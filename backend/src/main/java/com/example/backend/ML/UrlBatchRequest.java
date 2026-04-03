package com.example.backend.ML;

import java.util.List;

public class UrlBatchRequest {
    private List<String> urls;

    public UrlBatchRequest() {
    }

    public UrlBatchRequest(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
