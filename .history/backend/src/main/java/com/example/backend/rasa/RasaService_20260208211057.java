package com.example.backend.rasa;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;

@Service
public class RasaService {
    private final Path domainPath;

    public RasaService(@Value("${rasa.domain.path}") String domainPath) {
        this.domainPath = Path.of(domainPath);
    }

    public Map<String, List<String>> getResponseTexts() throws Exception {
        Yaml yaml = new Yaml();

        try (InputStream in = Files.newInputStream(domainPath)) {
            Object loaded = yaml.load(in);
            if (!(loaded instanceof Map<?, ?> root))
                return Map.of();

            Object responsesObj = root.get("responses");
            if (!(responsesObj instanceof Map<?, ?> responses))
                return Map.of();

            Map<String, List<String>> result = new LinkedHashMap<>();

            for (Map.Entry<?, ?> entry : responses.entrySet()) {
                String utterName = String.valueOf(entry.getKey());
                Object variationsObj = entry.getValue();

                List<String> texts = new ArrayList<>();

                if (variationsObj instanceof List<?> variations) {
                    for (Object item : variations) {
                        if (item instanceof Map<?, ?> m) {
                            Object textObj = m.get("text");
                            if (textObj != null)
                                texts.add(String.valueOf(textObj));
                        }
                    }
                }

                result.put(utterName, texts);
            }

            return result;
        }
    }

    public List<String> getTextsForUtter(String utterName) throws Exception {
        Map<String, List<String>> all = getResponseTexts();
        return all.getOrDefault(utterName, List.of());
    }
}
