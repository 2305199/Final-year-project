package com.example.backend.ML;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class MlClient {

    private final WebClient webClient;

    public MlClient(WebClient.Builder builder) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024) // 10 MB
                )
                .build();

        this.webClient = builder
                .baseUrl("http://localhost:8001")
                .exchangeStrategies(strategies)
                .build();
    }

    public Mono<String> predict(String url) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/predict")
                        .queryParam("url", url)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<UrlBatchResponse> predictBatch(List<String> urls) {
        UrlBatchRequest request = new UrlBatchRequest(urls);

        return webClient.post()
                .uri("/predict-batch")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UrlBatchResponse.class);
    }
}