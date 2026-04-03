package com.example.backend.ML;

import java.util.List;

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

    public Mono<String> predictBatch(List<String> urls) {

        UrlBatchRequest request = new UrlBatchRequest(urls);

        return webClient.post()
                .uri("/predict-batch")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    }

}
