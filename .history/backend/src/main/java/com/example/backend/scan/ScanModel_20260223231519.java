package com.example.backend.scan;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Scan")
public class ScanModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ScanID")
    private Long scanID;

    @Column(name = "ScanDate", nullable = false)
    private LocalDateTime scanDate;

    @Column(name = "TotalURLsScanned", nullable = false)
    private int totalURLsScanned;

    @Column(name = "PhishingURLsFound", nullable = false)
    private int phishingURLsFound;

    @Column(name = "SafeURLsFound", nullable = false)
    private int safeURLsFound;

    // ---- constructors ----
    public Scan() {}

    // ---- getters/setters ----
    public Long getScanID() {
        return scanID;
    }

    public void setScanID(Long scanID) {
        this.scanID = scanID;
    }

    public LocalDateTime getScanDate() {
        return scanDate;
    }

    public void setScanDate(LocalDateTime scanDate) {
        this.scanDate = scanDate;
    }

    public int getTotalURLsScanned() {
        return totalURLsScanned;
    }

    public void setTotalURLsScanned(int totalURLsScanned) {
        this.totalURLsScanned = totalURLsScanned;
    }

    public int getPhishingURLsFound() {
        return phishingURLsFound;
    }

    public void setPhishingURLsFound(int phishingURLsFound) {
        this.phishingURLsFound = phishingURLsFound;
    }

    public int getSafeURLsFound() {
        return safeURLsFound;
    }

    public void setSafeURLsFound(int safeURLsFound) {
        this.safeURLsFound = safeURLsFound;
    }
}