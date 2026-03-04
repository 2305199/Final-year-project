package com.example.backend.rasa;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlExtractor {

    // Matches https://..., http://..., and www....
    private static final Pattern URL_PATTERN = Pattern.compile("\\b(https?://[^\\s]+|www\\.[^\\s]+)\\b");

    // Extract URLs from a single text
    public static List<String> extractFromText(String text) {
        List<String> urls = new ArrayList<>();
        Matcher matcher = URL_PATTERN.matcher(text);

        while (matcher.find()) {
            String url = matcher.group(1);

            // Normalize www. URLs
            if (url.startsWith("www.")) {
                url = "https://" + url;
            }

            urls.add(url);
        }

        return urls;
    }

    // Extract URLs from the full Rasa response map
    public static Map<String, List<String>> extractFromResponses(
            Map<String, List<String>> responses) {

        Map<String, List<String>> result = new LinkedHashMap<>();

        for (Map.Entry<String, List<String>> entry : responses.entrySet()) {
            String utterName = entry.getKey();
            List<String> texts = entry.getValue();

            Set<String> urls = new LinkedHashSet<>();

            for (String text : texts) {
                urls.addAll(extractFromText(text));
            }

            if (!urls.isEmpty()) {
                result.put(utterName, new ArrayList<>(urls));
            }
        }

        return result;
    }
}
