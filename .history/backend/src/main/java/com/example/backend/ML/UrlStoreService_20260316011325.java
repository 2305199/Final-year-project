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

    public Mono<UrlBatchResponse> classifyAndStore( // Mono = task to be finished later
            List<String> urls) {

        return mlClient.predictBatch(urls)
                .publishOn(Schedulers.boundedElastic())
                .map(resp -> {

                    ScanModel scan = new ScanModel();
                    scan.setScanDate(LocalDateTime.now()); // store date/time of scan
                    scan.setTotalUrlsScanned(0); // storing initial phishing/safe/total counts as 0
                    scan.setPhishingUrlsFound(0);
                    scan.setSafeUrlsFound(0);

                    scan = scanRepository.save(scan); // storing scan to get the ID
                    Long scanId = scan.getId(); // getting ID to link URLs to this scan

                    int total = 0; // variables to count results for scan
                    int phishing = 0;
                    int safe = 0;

                    for (UrlPrediction r : resp.getResults()) { // loop through all URLs from ML response

                        total++; // increments total

                        String result = r.getPrediction().equalsIgnoreCase("phishing") // setting result as "phishing"
                                ? "phishing"
                                : "safe";

                        if ("phishing".equals(result)) { // if "phishing" == result plus 1
                            phishing++;
                        } else {
                            safe++; // else safe plus 1
                        }

                        UrlModel row = new UrlModel(); // create new database row for URL
                        row.setUrl(r.getUrl()); // store URL

                        row.setScanID(scanId); // links scan to URL with scanID
                        row.setResult(result); // store result

                        try {
                            String featuresJson = objectMapper
                                    .writeValueAsString(r.getFeatures()); // convert feaures as JSON
                            row.setFeaturesJson(featuresJson); // store features in database done as JSON so i didnt
                                                               // have to make 17 columns for each feature
                        } catch (Exception e) {

                            row.setFeaturesJson("{}"); // if error empty JSON stored
                        }

                        urlRepository.save(row); // save URL and results in database
                    }

                    scan.setTotalUrlsScanned(total); // stored total/phishig/safe URLs
                    scan.setPhishingUrlsFound(phishing);
                    scan.setSafeUrlsFound(safe);
                    scanRepository.save(scan); // update the counter from earlier with these results

                    return resp;
                });
    }
}