package com.example.backend.ML;

import jakarta.persistence.*;

@Entity
@Table(name = "URLs")
public class UrlModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long urlID;

    private Long messageID;
    private Long locationID;

    @Column(length = 2048, nullable = false)
    private String url;

    @Column(nullable = false)
    private String result; // "phishing" or "safe"

    @Column(name = "ScanID")
    private Long scanID;

    public Long getUrlID() {
        return urlID;
    }

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public Long getScanID() {
        return scanID;
    }

    public void setScanID(Long scanID) {
        this.scanID = scanID;
    }

    public Long getLocationID() {
        return locationID;
    }

    public void setLocationID(Long locationID) {
        this.locationID = locationID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}