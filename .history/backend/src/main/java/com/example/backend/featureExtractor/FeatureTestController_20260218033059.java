package com.example.backend.featureExtractor;

import com.example.backend.model.ModelService;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class FeatureTestController {

    private final FeatureExtractorService featureExtractor;
    private final ModelService modelService;

    public FeatureTestController(FeatureExtractorService featureExtractor,
            ModelService modelService) {
        this.featureExtractor = featureExtractor;
        this.modelService = modelService;
    }

    // 🔹 Existing endpoint (kept the same)
    @GetMapping("/features")
    public Map<String, Object> features(@RequestParam String url) throws URISyntaxException {
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

    // 🔥 NEW endpoint that also predicts
    @GetMapping("/predict")
    public Map<String, Object> predict(@RequestParam String url) throws Exception {

        Map<String, Object> out = new HashMap<>();

        int ip = featureExtractor.ip(url);
        int nb_at = featureExtractor.nb_at(url);
        int nb_qm = featureExtractor.nb_qm(url);
        int nb_and = featureExtractor.nb_and(url);
        int nb_semicolumn = featureExtractor.nb_semicolumn(url);
        int nb_www = featureExtractor.nb_www(url);
        double ratio_digits_host = featureExtractor.ratio_digits_host(url);
        int phish_hints = featureExtractor.phish_hints(url);

        // Temporary until implemented properly
        int tld_in_subdomain = 0;
        int brand_in_subdomain = 0;

        String prediction = modelService.predict(
                ip,
                nb_at,
                nb_qm,
                nb_and,
                nb_semicolumn,
                nb_www,
                ratio_digits_host,
                tld_in_subdomain,
                phish_hints,
                brand_in_subdomain);

        out.put("url", url);
        out.put("ip", ip);
        out.put("nb_at", nb_at);
        out.put("nb_qm", nb_qm);
        out.put("nb_and", nb_and);
        out.put("nb_semicolumn", nb_semicolumn);
        out.put("nb_www", nb_www);
        out.put("ratio_digits_host", ratio_digits_host);
        out.put("phish_hints", phish_hints);
        out.put("prediction", prediction);

        return out;
    }
}
