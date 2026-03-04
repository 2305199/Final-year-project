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
                .publishOn(Schedulers.boundedElastic())
                .map(resp -> {

                    // 1) Create Scan row first
                    ScanModel scan = new ScanModel();
                    scan.setScanDate(LocalDateTime.now());
                    scan.setTotalURLsScanned(0);
                    scan.setPhishingURLsFound(0);
                    scan.setSafeURLsFound(0);

                    scan = scanRepository.save(scan); // generates ScanID

                    int total = 0;
                    int phishing = 0;
                    int safe = 0;

                    // 2) Save each URL row linked to ScanID
                    for (UrlPrediction r : resp.getResults()) {
                        total++;

                        String result = r.getPrediction().equalsIgnoreCase("phishing")
                                ? "phishing"
                                : "safe";

                        if (result.equals("phishing"))
                            phishing++;
                        else
                            safe++;

                        UrlModel row = new UrlModel();
                        row.setUrl(r.getUrl());
                        row.setMessageID(messageID);
                        row.setLocationID(locationID);

                        // ✅ link this URL to the scan
                        row.setScanID(scan.getScanID());

                        row.setResult(result);

                        urlRepository.save(row);
                    }

                    // 3) Update Scan totals
                    scan.setTotalURLsScanned(total);
                    scan.setPhishingURLsFound(phishing);
                    scan.setSafeURLsFound(safe);
                    scanRepository.save(scan);

                    return resp;
                });
    }
}