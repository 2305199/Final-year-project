import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ScanService {

  private final ProviderConfigRepository configRepo;
  private final WebClient webClient;

  public ScanService(ProviderConfigRepository configRepo, WebClient.Builder builder) {
    this.configRepo = configRepo;
    this.webClient = builder.build();
  }

  public String scanNow(Long configId, String userId, int limit) {
    ProviderConfig cfg = configRepo.findById(configId)
        .orElseThrow(() -> new RuntimeException("Provider config not found: " + configId));

    String url = buildLogsUrl(cfg, userId, limit);

    return webClient.get()
        .uri(url)
        .headers(h -> applyAuthHeaders(h, cfg))
        .retrieve()
        .bodyToMono(String.class) // return raw JSON string
        .block();
  }

  private String buildLogsUrl(ProviderConfig cfg, String userId, int limit) {
    String base = cfg.getBaseUrl();
    String path = cfg.getLogsPath();

    // clean slashes so you don’t end up with // or missing /
    if (base.endsWith("/"))
      base = base.substring(0, base.length() - 1);
    if (!path.startsWith("/"))
      path = "/" + path;

    // build query string
    return base + path + "?userId=" + userId + "&limit=" + limit;
  }

  private void applyAuthHeaders(HttpHeaders h, ProviderConfig cfg) {
    String type = cfg.getAuthType();

    if ("BEARER".equalsIgnoreCase(type)) {
      h.add("Authorization", "Bearer " + cfg.getAuthSecret());
    } else if ("API_KEY".equalsIgnoreCase(type)) {
      h.add("X-API-KEY", cfg.getAuthSecret());
    } else if ("NONE".equalsIgnoreCase(type)) {
      // no headers needed
    } else {
      throw new RuntimeException("Unsupported authType: " + type);
    }
  }
}
