package com.example.backend.featureExtractor;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class FeatureExtractorService {

    String aURL = "http://shadetreetechnology.com/V4/validation/a111aedc8ae390eabcfa130e041a10a4";

    public int ip(int ip) throws URISyntaxException {
        URI url = new URI(aURL);

        String hostname = url.getHost();

        System.out.println("Hostname: " + hostname);

        return ip;
    }

}
