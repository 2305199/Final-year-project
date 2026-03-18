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
        ExchangeStrategies strategies = ExchangeStrategies.builder() // a object to create larger size for ML response
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(30 * 1024 * 1024) // size set to 30MB
                )
                .build(); // the configuration is made

        this.webClient = builder // making web client
                .baseUrl("http://localhost:8001") // base adress of ML is set requests go here
                .exchangeStrategies(strategies) // applies the 30MB limit made
                .build();
    }

    public Mono<String> predict(String url) { // sending 1 URL to ml, return string phishing/safe
        return webClient.get() // create get request
                .uri(uriBuilder -> uriBuilder
                        .path("/predict")
                        .queryParam("url", url) // build request of URL to
                        .build())
                .retrieve() // does the request and get response
                .bodyToMono(String.class); // converts reponse to String
    }

    public Mono<UrlBatchResponse> predictBatch(List<String> urls) { // for many URLs
        UrlBatchRequest request = new UrlBatchRequest(urls); // creates object of many URLs

        return webClient.post() // sends post request to ML
                .uri("/predict-batch") // endpoint being sent to
                .bodyValue(request) // attach the request body
                .retrieve() // send the request get response
                .bodyToMono(UrlBatchResponse.class); // convert response to object
    }
}