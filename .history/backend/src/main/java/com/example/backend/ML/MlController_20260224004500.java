package com.example.backend.ML;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/ml")
@CrossOrigin(origins = "http://localhost:5173")
public class MlController {

    private final MlClient mlClient;

    public MlController(MlClient mlClient) {
        this.mlClient = mlClient;
    }

    // Predict ONE URL (no DB)
    @GetMapping("/predict")
    public Mono<String> predict(@RequestParam String url) {
        return mlClient.predict(url);
    }

    // Predict MANY URLs (no DB) – optional, only if you need it later
    @PostMapping("/predict-batch")
    public Mono<UrlBatchResponse> predictBatch(@RequestBody UrlBatchRequest request) {
        return mlClient.predictBatch(request.getUrls());
    }
}