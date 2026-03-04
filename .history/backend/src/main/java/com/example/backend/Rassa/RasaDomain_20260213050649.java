package com.example.backend.Rassa;

import java.util.List;
import java.util.Map;

public class RasaDomain {

    private Map<String, List<Map<String, Object>>> responses; // mapping the responses to objects (utter_name -> list of
                                                              // dicts)

    public Map<String, List<Map<String, Object>>> getResponses() { // returns the raw responses mapping (utter_name ->
                                                                   // list of dicts)
        return responses;
    }

    public void setResponses(Map<String, List<Map<String, Object>>> responses) {
        this.responses = responses; // setter for responses (used by SnakeYAML to populate the field when loading
                                    // domain.yml)
    }
}