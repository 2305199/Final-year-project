package com.example.backend.ML;

import com.example.backend.Rassa.RasaService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@RestController
@RequestMapping("/api/ml")
public class MlController {

    private final MlClient mlClient;
    private final RasaService rasaService;

    public MlController(MlClient mlClient, RasaService rasaService) {
        this.mlClient = mlClient;
        this.rasaService = rasaService;
    }

    @GetMapping("/predict")
    public Mono<String> predict(@RequestParam String url) { // Takes URL as parameter and returns the prediction as a
                                                            // string
        return mlClient.predict(url);
    }

    @GetMapping("/predict-extracted")
    public Mono<UrlBatchResponse> predictExtracted() throws Exception {
        List<String> urls = rasaService.extractAllUrls();
        List<String> uniqueUrls = new ArrayList<>(new LinkedHashSet<>(urls));
        return mlClient.predictBatch(uniqueUrls);
    }

}
