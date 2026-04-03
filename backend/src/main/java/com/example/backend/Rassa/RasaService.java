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
import java.util.stream.Collectors; // transform collections using streams and collectors

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import java.util.regex.Matcher;

@Service
public class RasaService {

    private final Path domainPath; // going to be used to store the file path

    public RasaService(@Value("${rasa.domain.path}") String domainPath) {
        this.domainPath = Path.of(domainPath); // converts the string path into object path so java can read it
    }

    public RasaDomain loadDomain() throws Exception {
        Constructor constructor = new Constructor(RasaDomain.class); // load domain.yml and convert to RasaDomain object

        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setSkipMissingProperties(true);
        constructor.setPropertyUtils(propertyUtils); // ignore any properties in the YAML that not in RasaDomain class

        Yaml yaml = new Yaml(constructor); // create a new Yaml parser to create the object rasadomain

        try (InputStream in = Files.newInputStream(domainPath)) { // open the domain file as stream
            return yaml.loadAs(in, RasaDomain.class); // read the stream and convert to the object
        }
    }

    // returns utter_name -> list of text strings
    public Map<String, List<String>> getResponseTexts() throws Exception { // extract messages from domain object
        RasaDomain domain = loadDomain(); // load domain into memory
        System.out.println("Loaded responses keys: " + domain.getResponses().keySet());

        if (domain == null || domain.getResponses() == null) {
            return Map.of();

        }

        // Convert Utterance objects into String text
        return domain.getResponses().entrySet().stream()
                .collect(Collectors.toMap( // make responses into map
                        Map.Entry::getKey, // key is utterance name
                        e -> e.getValue().stream() // loop through responses
                                .map(m -> m.get("text")) // extract text field from each repsonse
                                .filter(Objects::nonNull)
                                .map(String::valueOf) // convert to string
                                .toList()));

    }

    public List<String> getTextsForUtter(String utterName) throws Exception {
        return getResponseTexts().getOrDefault(utterName, List.of()); // get texts for specfic utterance
    }

    public List<String> extractAllUrls() throws Exception {

        Map<String, List<String>> responses = getResponseTexts(); // get all texts

        if (responses == null || responses.isEmpty()) { // if nothing then return empty
            return List.of();
        }

        Pattern pattern = Pattern.compile(
                "(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)"); // regex pattern to match URLs

        List<String> urls = new ArrayList<>();

        for (List<String> texts : responses.values()) { // First loop (lists)
            for (String text : texts) { // Second loop (strings)

                Matcher matcher = pattern.matcher(text);

                while (matcher.find()) { // when URL found add to ArrayList urls
                    urls.add(matcher.group());
                }
            }
        }

        return urls; // return arraylist urls
    }

}
