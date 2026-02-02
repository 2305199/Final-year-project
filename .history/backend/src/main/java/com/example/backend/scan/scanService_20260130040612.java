package com.example.backend.scan;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.backend.chatbotMessages.chatbotMessagesModel;
import com.example.backend.chatbotMessages.chatbotMessagesRepository;
import com.example.backend.providerConfig.ProviderConfig;
import com.example.backend.providerConfig.ProviderConfigRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScanService {

  private final ProviderConfigRepository configRepo;
  private final chatbotMessagesRepository chatbotMessageRepo;
  private final WebClient webClient;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public ScanService(ProviderConfigRepository configRepo,
      chatbotMessagesRepository chatbotMessageRepo,
      WebClient.Builder builder) {
    this.configRepo = configRepo;
    this.chatbotMessageRepo = chatbotMessageRepo;
    this.webClient = builder.build();
  }

  public String scanNow(Long configId, String userId, int limit) {
    ProviderConfig cfg = configRepo.findById(configId)
        .orElseThrow(() -> new RuntimeException("Provider config not found: " + configId));

    String url = buildLogsUrl(cfg, userId, limit);

    String json = webClient.get()
        .uri(url)
        .headers(h -> applyHeaders(h, cfg))
        .retrieve()
        .onStatus(status -> status.isError(), resp -> resp.bodyToMono(String.class)
            .defaultIfEmpty("")
            .map(body -> new RuntimeException(
                "External API error: " + resp.statusCode() + " URL=" + url + " body=" + body)))
        .bodyToMono(String.class)
        .block();

    String safeJson = (json == null) ? "" : json;

    // ✅ Save Reddit messages to DB
    if ("REDDIT".equalsIgnoreCase(cfg.getProviderName()) && !safeJson.isBlank()) {
      saveRedditMessagesToDb(safeJson);
    }

    return safeJson;
  }

  private String buildLogsUrl(ProviderConfig cfg, String userId, int limit) {
    String base = cfg.getBaseUrl();
    String path = cfg.getLogsPath();

    if (base.endsWith("/"))
      base = base.substring(0, base.length() - 1);
    if (!path.startsWith("/"))
      path = "/" + path;

    // Reddit: ignore userId, just use limit
    if ("REDDIT".equalsIgnoreCase(cfg.getProviderName())) {
      return base + path + "?limit=" + limit;
    }

    // Default: userId + limit
    String encodedUserId = URLEncoder.encode(userId, StandardCharsets.UTF_8);
    return base + path + "?userId=" + encodedUserId + "&limit=" + limit;
  }

  private void applyHeaders(HttpHeaders h, ProviderConfig cfg) {

    // Reddit often requires a descriptive User-Agent
    if ("REDDIT".equalsIgnoreCase(cfg.getProviderName())) {
      h.add("User-Agent", "ZiaRahman-UniProject/1.0 (contact: zia@example.com)");
    }

    String type = cfg.getAuthType();
    if ("BEARER".equalsIgnoreCase(type)) {
      h.add("Authorization", "Bearer " + cfg.getAuthSecret());
    } else if ("API_KEY".equalsIgnoreCase(type)) {
      h.add("X-API-KEY", cfg.getAuthSecret());
    } else if ("NONE".equalsIgnoreCase(type)) {
      // no auth needed
    } else {
      throw new RuntimeException("Unsupported authType: " + type);
    }
  }

  // ✅ Reddit JSON -> chatbot_messages table
  private void saveRedditMessagesToDb(String json) {
    try {
      JsonNode root = objectMapper.readTree(json);
      JsonNode children = root.path("data").path("children");

      if (!children.isArray())
        return;

      for (JsonNode child : children) {
        String body = child.path("data").path("body").asText(null);
        long createdUtc = child.path("data").path("created_utc").asLong(0);

        if (body == null || body.isBlank())
          continue;

        chatbotMessagesModel msg = new chatbotMessagesModel();
        msg.setMessageText(body);

        Instant time = createdUtc > 0 ? Instant.ofEpochSecond(createdUtc) : Instant.now();
        msg.setDate(time);

        chatbotMessageRepo.save(msg);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to parse Reddit JSON and save messages", e);
    }
  }
}
