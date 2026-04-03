package com.example.backend.featureExtractor;

import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class FeatureTestController {

    private final FeatureExtractorService featureExtractor;

    public FeatureTestController(FeatureExtractorService featureExtractor) {
        this.featureExtractor = featureExtractor;
    }

    @GetMapping("/features")
    public Map<String, Object> test(@RequestParam String url) throws URISyntaxException {
        Map<String, Object> out = new HashMap<>();
        out.put("url", url);
        out.put("ip", featureExtractor.ip(url));
        out.put("nb_at", featureExtractor.nb_at(url));
        out.put("nb_qm", featureExtractor.nb_qm(url));
        out.put("nb_and", featureExtractor.nb_and(url));
        out.put("nb_semicolumn", featureExtractor.nb_semicolumn(url));
        out.put("nb_www", featureExtractor.nb_www(url));
        out.put("ratio_digits_host", featureExtractor.ratio_digits_host(url));
        out.put("phish_hints", featureExtractor.phish_hints(url));
        out.put("length_url", featureExtractor.length_url(url));
        out.put("nb_dots", featureExtractor.nb_dots(url));
        return out;
    }
}
