package com.example.backend.Rassa;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import java.util.regex.Matcher;

@Service
public class RasaService {

    private final Path domainPath;

    public RasaService(@Value("${rasa.domain.path}") String domainPath) {
        this.domainPath = Path.of(domainPath);
    }

    public RasaDomain loadDomain() throws Exception {
        Constructor constructor = new Constructor(RasaDomain.class);

        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setSkipMissingProperties(true); // ignore version, intents, etc.
        constructor.setPropertyUtils(propertyUtils);

        Yaml yaml = new Yaml(constructor);

        try (InputStream in = Files.newInputStream(domainPath)) {
            return yaml.loadAs(in, RasaDomain.class);
        }
    }

    // returns utter_name -> list of text strings (clean output)
    public Map<String, List<String>> getResponseTexts() throws Exception {
        RasaDomain domain = loadDomain();
        System.out.println("Loaded responses keys: " + domain.getResponses().keySet());

        if (domain == null || domain.getResponses() == null) {
            return Map.of();

        }

        // Convert Utterance objects -> String text
        return domain.getResponses().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(m -> m.get("text"))
                                .filter(Objects::nonNull)
                                .map(String::valueOf)
                                .toList()));

    }

    public List<String> getTextsForUtter(String utterName) throws Exception {
        return getResponseTexts().getOrDefault(utterName, List.of());
    }

    public List<String> extractAllUrls() throws Exception {

        Map<String, List<String>> responses = getResponseTexts();

        if (responses == null || responses.isEmpty()) {
            return List.of();
        }

        Pattern pattern = Pattern.compile(
                "(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)");

        List<String> urls = new ArrayList<>();

        for (List<String> texts : responses.values()) { // First loop (lists)
            for (String text : texts) { // Second loop (strings)

                Matcher matcher = pattern.matcher(text);

                while (matcher.find()) {
                    urls.add(matcher.group());
                }
            }
        }

        return urls;
    }

}
