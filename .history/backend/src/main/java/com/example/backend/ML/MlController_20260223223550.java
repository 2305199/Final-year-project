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
    private final UrlStoreService urlStoreService; // ✅ ADD

    // ✅ UPDATE constructor to include UrlStoreService
    public MlController(MlClient mlClient, RasaService rasaService, UrlStoreService urlStoreService) {
        this.mlClient = mlClient;
        this.rasaService = rasaService;
        this.urlStoreService = urlStoreService;
    }

    @GetMapping("/predict")
    public Mono<String> predict(@RequestParam String url) {
        return mlClient.predict(url);
    }

    @GetMapping("/predict-extracted")
    public Mono<UrlBatchResponse> predictExtracted() throws Exception {
        List<String> urls = rasaService.extractAllUrls();
        List<String> uniqueUrls = new ArrayList<>(new LinkedHashSet<>(urls));
        return mlClient.predictBatch(uniqueUrls);
    }

    @GetMapping("/predict-extracted-store")
    public Mono<UrlBatchResponse> predictExtractedAndStore() throws Exception {
        List<String> urls = rasaService.extractAllUrls();
        List<String> uniqueUrls = new ArrayList<>(new LinkedHashSet<>(urls));

        Long messageID = 1L;
        Long scanID = 1L;
        Long locationID = 1L;

        return urlStoreService.classifyAndStore(uniqueUrls, messageID, scanID, locationID);
    }
}