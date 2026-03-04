package com.example.backend.scan;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.example.backend.ML.UrlModel;
import com.example.backend.ML.UrlRepository;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class ScanController {

    private final ScanRepository scanRepository;
    private final UrlRepository urlRepository;

    public ScanController(ScanRepository scanRepository, UrlRepository urlRepository) {
        this.scanRepository = scanRepository;
        this.urlRepository = urlRepository;
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
}