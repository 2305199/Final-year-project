package com.example.backend.scan;

import java.util.ArrayList;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.example.backend.ML.UrlBatchResponse;
import com.example.backend.ML.UrlModel;
import com.example.backend.ML.UrlRepository;
import com.example.backend.ML.UrlStoreService;
import com.example.backend.Rassa.RasaService;

import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:5173") // or react blocks
@RestController
@RequestMapping("/api")
public class ScanController {

    private final ScanRepository scanRepository;
    private final UrlRepository urlRepository;
    private final RasaService rasaService;
    private final UrlStoreService urlStoreService;

    public ScanController(
            ScanRepository scanRepository,
            UrlRepository urlRepository,
            RasaService rasaService,
            UrlStoreService urlStoreService) {
        this.scanRepository = scanRepository;
        this.urlRepository = urlRepository;
        this.rasaService = rasaService;
        this.urlStoreService = urlStoreService;
    }

    // History page getting all scans
    @GetMapping("/scans")
    public List<ScanModel> getAllScans() {
        return scanRepository.findAll(Sort.by(Sort.Direction.DESC, "id")); // get all scans sort by scanID and
                                                                           // descending order
    }

    // when URLs for scan
    @GetMapping("/scans/{scanId}/urls")
    public List<UrlModel> getUrlsForScan(@PathVariable Long scanId) { // get all URLs for scan
        return urlRepository.findByScanID(scanId); // get URLs based off scan ID
    }

    @PostMapping("/scans/run")
    public Mono<Void> runScan() throws Exception { // run scan and result returnd to frontend
        List<String> urls = rasaService.extractAllUrls(); // get all URLs from chatbot
        List<String> scanUrls = new ArrayList<>(urls); // store URLs as list

        return urlStoreService.classifyAndStore(scanUrls).then(); // sends to ML then result saved to database and
                                                                  // returned to frontend
    }
}