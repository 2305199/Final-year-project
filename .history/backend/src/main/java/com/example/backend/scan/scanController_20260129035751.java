package com.example.backend.scan;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scan")
public class scanController {

    private final ScanService scanService;

    public scanController(ScanService scanService) {
        this.scanService = scanService;
    }

    // React: POST /api/scan/1?userId=123&limit=50
    @PostMapping("/{configId}")
    public String scan(@PathVariable Long configId,
            @RequestParam String userId,
            @RequestParam(defaultValue = "50") int limit) {

        return scanService.scanNow(configId, userId, limit);
    }
}
