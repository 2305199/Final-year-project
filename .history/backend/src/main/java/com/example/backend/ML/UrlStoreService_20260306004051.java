package com.example.backend.ML;

import com.example.backend.scan.ScanModel;
import com.example.backend.scan.ScanRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlStoreService {

    private final MlClient mlClient;
    private final UrlRepository urlRepository;
    private final ScanRepository scanRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UrlStoreService(MlClient mlClient,
            UrlRepository urlRepository,
            ScanRepository scanRepository) {
        this.mlClient = mlClient;
        this.urlRepository = urlRepository;
        this.scanRepository = scanRepository;
    }

    public Mono<UrlBatchResponse> classifyAndStore(
            List<String> urls,
            Long messageID,
            Long locationID) {

        return mlClient.predictBatch(urls)
                .publishOn(Schedulers.boundedElastic())
                .map(resp -> {

                    ScanModel scan = new ScanModel();
                    scan.setScanDate(LocalDateTime.now());
                    scan.setTotalUrlsScanned(0);
                    scan.setPhishingUrlsFound(0);
                    scan.setSafeUrlsFound(0);

                    scan = scanRepository.save(scan);
                    Long scanId = scan.getId();

                    int total = 0;
                    int phishing = 0;
                    int safe = 0;

                    for (UrlPrediction r : resp.getResults()) {

                        total++;

                        String result = r.getPrediction().equalsIgnoreCase("phishing")
                                ? "phishing"
                                : "safe";

                        if ("phishing".equals(result)) {
                            phishing++;
                        } else {
                            safe++;
                        }

                        UrlModel row = new UrlModel();
                        row.setUrl(r.getUrl());

                        row.setScanID(scanId);
                        row.setResult(result);

                        try {
                            String featuresJson = objectMapper
                                    .writeValueAsString(r.getFeatures());
                            row.setFeaturesJson(featuresJson);
                        } catch (Exception e) {

                            row.setFeaturesJson("{}");
                        }

                        urlRepository.save(row);
                    }

                    scan.setTotalUrlsScanned(total);
                    scan.setPhishingUrlsFound(phishing);
                    scan.setSafeUrlsFound(safe);
                    scanRepository.save(scan);

                    return resp;
                });
    }
}