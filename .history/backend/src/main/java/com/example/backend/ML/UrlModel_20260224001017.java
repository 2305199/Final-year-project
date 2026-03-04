package com.example.backend.ML;

import jakarta.persistence.*;

@Entity
@Table(name = "urls")
public class UrlModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "urlid")
    private Long urlID;

    @Column(name = "messageid")
    private Long messageID;

    @Column(name = "locationid")
    private Long locationID;

    @Column(name = "url", length = 2048, nullable = false)
    private String url;

    @Column(name = "result", nullable = false)
    private String result; // "phishing" or "safe"

    @Column(name = "scanid")
    private Long scanID;

    // getters & setters

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