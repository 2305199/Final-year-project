package com.example.backend.ML;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class UrlStoreService {

    private final MlClient mlClient;
    private final UrlRepository urlRepository;

    public UrlStoreService(MlClient mlClient, UrlRepository urlRepository) {
        this.mlClient = mlClient;
        this.urlRepository = urlRepository;
    }

    public Mono<UrlBatchResponse> classifyAndStore(
            List<String> urls,
            Long messageID,
            Long scanID,
            Long locationID) {
        return mlClient.predictBatch(urls)
                .publishOn(Schedulers.boundedElastic())
                .map(resp -> {

                    for (UrlPrediction r : resp.getResults()) {

                        UrlModel row = new UrlModel();
                        row.setUrl(r.getUrl());
                        row.setMessageID(messageID);
                        row.setScanID(scanID);
                        row.setLocationID(locationID);

                        // python: "phishing" / "legitimate"
                        // db: "phishing" / "safe"
                        String result = r.getPrediction().equalsIgnoreCase("phishing")
                                ? "phishing"
                                : "safe";

                        row.setResult(result);

                        urlRepository.save(row);
                    }

                    return resp;
                });
    }
}