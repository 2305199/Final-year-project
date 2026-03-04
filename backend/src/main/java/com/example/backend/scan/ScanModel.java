package com.example.backend.scan;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scan")
public class ScanModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // ✅ IMPORTANT: your DB column is id (not scanid)
    private Long id;

    @Column(name = "scan_date")
    private LocalDateTime scanDate;

    @Column(name = "totalurls_scanned")
    private Integer totalUrlsScanned;

    @Column(name = "phishing_urls_found")
    private Integer phishingUrlsFound;

    @Column(name = "safe_urls_found")
    private Integer safeUrlsFound;

    // getters/setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getScanDate() {
        return scanDate;
    }

    public void setScanDate(LocalDateTime scanDate) {
        this.scanDate = scanDate;
    }

    public int getTotalUrlsScanned() {
        return totalUrlsScanned;
    }

    public void setTotalUrlsScanned(int totalUrlsScanned) {
        this.totalUrlsScanned = totalUrlsScanned;
    }

    public int getPhishingUrlsFound() {
        return phishingUrlsFound;
    }

    public void setPhishingUrlsFound(int phishingUrlsFound) {
        this.phishingUrlsFound = phishingUrlsFound;
    }

    public int getSafeUrlsFound() {
        return safeUrlsFound;
    }

    public void setSafeUrlsFound(int safeUrlsFound) {
        this.safeUrlsFound = safeUrlsFound;
    }
}