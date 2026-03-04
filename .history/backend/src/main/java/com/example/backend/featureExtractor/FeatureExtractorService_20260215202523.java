package com.example.backend.featureExtractor;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class FeatureExtractorService {

    public int ip(String URL) throws URISyntaxException {

        URI url = new URI(URL);

        String hostname = url.getHost();

        System.out.println("Hostname: " + hostname);

        String parts[] = hostname.split("\\.");

        if (parts.length != 4) {
            return 0;

        }
        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return 0;
                }
            } catch (NumberFormatException e) {
                return 0;
            }

        }

        return 1;

    }

}
