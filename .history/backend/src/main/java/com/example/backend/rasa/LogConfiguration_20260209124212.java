package com.example.backend.RASA;

import java.util.List;
import java.util.Map;

public class LogConfiguration {
    private Map<String, String> paths;
    private String packagePrefix;

    public Map<String, List<Utterance>> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, List<Utterance>> responses) {
        this.responses = responses;
    }

    public String getPackagePrefix() {
        return packagePrefix;
    }

    public void setPackagePrefix(String packagePrefix) {
        this.packagePrefix = packagePrefix;
    }

    @Override
    public String toString() {
        return "LogConfiguration [paths=" + paths + "]";
    }
}