package com.example.backend.scan;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.example.backend.ML.UrlBatchResponse;
import com.example.backend.ML.UrlModel;
import com.example.backend.ML.UrlRepository;
import com.example.backend.ML.UrlStoreService;
import com.example.backend.Rassa.RasaService;

import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:5173")
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

    // ✅ History page - list all scans
    @GetMapping("/scans")
    public List<ScanModel> getAllScans() {
        return scanRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    // ✅ When clicking a scan - show its URLs
    @GetMapping("/scans/{scanId}/urls")
    public List<UrlModel> getUrlsForScan(@PathVariable Long scanId) {
        return urlRepository.findByScanID(scanId);
    }

    // ✅ NEW: Run a scan (domain.yml -> extract urls -> python ML -> store DB)
    @PostMapping("/scans/run")
    public Mono<UrlBatchResponse> runScan() throws Exception {
        List<String> urls = rasaService.extractAllUrls();
        List<String> uniqueUrls = new ArrayList<>(new LinkedHashSet<>(urls));

        Long messageID = 1L;
        Long locationID = 1L;

        return urlStoreService.classifyAndStore(uniqueUrls, messageID, locationID);
    }
}