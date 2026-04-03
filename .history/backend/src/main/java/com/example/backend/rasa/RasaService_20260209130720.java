package com.example.backend.RASA;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;

@Service
public class RasaService {

    private final Path domainPath;

    public RasaService(@Value("${rasa.domain.path}") String domainPath) {
        this.domainPath = Path.of(domainPath);
    }

    public Map<String, List<String>> getResponses() throws Exception {

        // ✅ Proper way to ignore unknown keys like "version", "intents", etc.
        Constructor constructor = new Constructor(LogConfiguration.class);
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setSkipMissingProperties(true);
        constructor.setPropertyUtils(propertyUtils);

        Yaml yaml = new Yaml(constructor);

        LogConfiguration config;
        try (InputStream in = Files.newInputStream(domainPath)) {
            config = yaml.loadAs(in, LogConfiguration.class);
        }

        if (config == null || config.getResponses() == null) {
            return Map.of();
        }

        return config.getResponses().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(Utterance::getText)
                                .collect(Collectors.toList()),
                        (a, b) -> a,
                        java.util.LinkedHashMap::new));
    }

    public List<String> getUtterance(String utterName) throws Exception {
        return getResponses().getOrDefault(utterName, List.of());
    }
}
