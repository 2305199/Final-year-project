package com.example.backend.Rassa;

import java.util.List;
import java.util.Map;

public class RasaDomain {

    private Map<String, List<Map<String, Object>>> responses;

    public Map<String, List<Map<String, Object>>> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, List<Map<String, Object>>> responses) {
        this.responses = responses;
    }
}