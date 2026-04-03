package com.example.backend.folder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ml")
public class MlController {

    private final MlClient mlClient;

    public MlController(MlClient mlClient) {
        this.mlClient = mlClient;
    }

    @GetMapping("/predict")
    public Mono<String> predict(@RequestParam String url) {
        return mlClient.predict(url);
    }
}
