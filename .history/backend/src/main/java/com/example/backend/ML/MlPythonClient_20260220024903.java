package com.example.backend.ML;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MlPythonClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8001";

    public UrlBatchResponse predictBatch(List<String> urls) {
        UrlBatchRequest request = new UrlBatchRequest(urls);
        return restTemplate.postForObject(
                baseUrl + "/predict-batch",
                request,
                UrlBatchResponse.class);
    }
}
