package com.example.backend.Rassa;

import java.util.List;
import java.util.Map;

public class RasaDomain {

    private Map<String, List<Utterance>> responses;

    public Map<String, List<Utterance>> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, List<Utterance>> responses) {
        this.responses = responses;
    }
}