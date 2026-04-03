package com.example.backend.Rassa;

import com.example.backend.ML.MlPythonClient;
import com.example.backend.ML.UrlBatchResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class BridgeService {

    private final RasaService rasaService;
    private final MlPythonClient mlClient;

    public BridgeService(RasaService rasaService, MlPythonClient mlClient) {
        this.rasaService = rasaService;
        this.mlClient = mlClient;
    }

    public UrlBatchResponse classifyExtractedUrls() throws Exception {
        List<String> urls = rasaService.extractAllUrls();

        // remove duplicates (recommended)
        List<String> uniqueUrls = new ArrayList<>(new LinkedHashSet<>(urls));

        return mlClient.predictBatch(uniqueUrls);
    }
}
