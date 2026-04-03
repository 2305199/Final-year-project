package com.example.backend.ML;

import com.example.backend.scan.ScanModel;
import com.example.backend.scan.ScanRepository;
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

    public UrlStoreService(MlClient mlClient, UrlRepository urlRepository, ScanRepository scanRepository) {
        this.mlClient = mlClient;
        this.urlRepository = urlRepository;
        this.scanRepository = scanRepository;
    }

    public Mono<UrlBatchResponse> classifyAndStore(
            List<String> urls,
            Long messageID,
            Long locationID) {
        return mlClient.predictBatch(urls)
                // move blocking JPA work onto elastic threads
                .publishOn(Schedulers.boundedElastic())
                .map(resp -> {

                    // 1) Create Scan row first (so we get an auto-increment id)
                    ScanModel scan = new ScanModel();
                    scan.setScanDate(LocalDateTime.now());
                    scan.setTotalUrlsScanned(0);
                    scan.setPhishingUrlsFound(0);
                    scan.setSafeUrlsFound(0);

                    scan = scanRepository.save(scan); // ✅ generates id
                    Long scanId = scan.getId(); // ✅ use id, not scanID

                    int total = 0;
                    int phishing = 0;
                    int safe = 0;

                    // 2) Save each URL row linked to this scan
                    for (UrlPrediction r : resp.getResults()) {
                        total++;

                        String result = r.getPrediction().equalsIgnoreCase("phishing")
                                ? "phishing"
                                : "safe";

                        if ("phishing".equals(result))
                            phishing++;
                        else
                            safe++;

                        UrlModel row = new UrlModel();
                        row.setUrl(r.getUrl());
                        row.setMessageID(messageID);
                        row.setLocationID(locationID);

                        // ✅ link this URL row to scan id
                        row.setScanID(scanId);

                        row.setResult(result);

                        urlRepository.save(row);
                    }

                    // 3) Update scan totals
                    scan.setTotalUrlsScanned(total);
                    scan.setPhishingUrlsFound(phishing);
                    scan.setSafeUrlsFound(safe);
                    scanRepository.save(scan);

                    return resp;
                });
    }
}