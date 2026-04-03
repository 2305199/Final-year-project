package com.example.backend.RASA;

import java.util.List;
import java.util.Map;

public class LogConfiguration {

    private Map<String, List<Utterance>> responses;

    public Map<String, List<Utterance>> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, List<Utterance>> responses) {
        this.responses = responses;
    }

    @Override
    public String toString() {
        return "RasaDomain [responses=" + responses + "]";
    }
}