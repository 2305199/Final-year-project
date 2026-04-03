package com.example.backend.ML;

import java.util.Map;

public class UrlPrediction {
    private String url;
    private String prediction;

    // was List<Double>
    private Map<String, Object> features;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public Map<String, Object> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Object> features) {
        this.features = features;
    }
}