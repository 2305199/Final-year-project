package com.example.backend.folder;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class MlClient {

    private final WebClient webClient;

    public MlClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8001").build();
    }

    public Mono<String> predict(String url) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/predict")
                        .queryParam("url", url)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
