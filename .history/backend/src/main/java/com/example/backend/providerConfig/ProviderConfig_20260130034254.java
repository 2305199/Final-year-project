package com.example.backend.providerConfig;

import jakarta.persistence.*;

@Entity
@Table(name = "provider_config")
public class ProviderConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String baseUrl = ""; // https://...

    @Column(nullable = false)
    private String logsPath; // /api/...

    // "BEARER", "API_KEY", "NONE"
    @Column(nullable = false)
    private String authType;

    @Column(length = 2048)
    private String authSecret;

    @Column(name = "provider_name")
    private String providerName;

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Long getId() {
        return id;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getLogsPath() {
        return logsPath;
    }

    public void setLogsPath(String logsPath) {
        this.logsPath = logsPath;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAuthSecret() {
        return authSecret;
    }

    public void setAuthSecret(String authSecret) {
        this.authSecret = authSecret;
    }
}
