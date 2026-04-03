package com.example.backend.ML;

import java.util.List;

public class UrlBatchResponse {
    private int count;
    private List<UrlPrediction> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<UrlPrediction> getResults() {
        return results;
    }

    public void setResults(List<UrlPrediction> results) {
        this.results = results;
    }
}
