package com.example.backend.featureExtractor;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class FeatureExtractorService {

    ArrayList<String> featuresList = new ArrayList<String>();

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

    public int nb_at(String URL) {
        int count = 0;
        for (char c : URL.toCharArray()) {
            if (c == '@') {
                count++;
            }
        }

        return count;
    }

    public int nb_qm(String URL) {
        int count = 0;
        for (char c : URL.toCharArray()) {
            if (c == '?') {
                count++;
            }
        }
        return count;
    }

    public int nb_and(String URL) {
        int count = 0;
        for (char c : URL.toCharArray()) {
            if (c == '&') {
                count++;
            }
        }
        return count;
    }

    public int nb_semicolumn(String URL) {
        int count = 0;
        for (char c : URL.toCharArray()) {
            if (c == ';') {
                count++;
            }
        }
        return count;
    }

    public int nb_www(String URL) {
        int count = 0;
        for (int i = 0; i < URL.length() - 2; i++) {
            if (URL.substring(i, i + 3).equals("www")) {
                count++;
            }
        }
        return count;
    }

    public double ratio_digits_host(String URL) throws URISyntaxException {
        URI url = new URI(URL);

        String hostname = url.getHost();

        if (hostname == null || hostname.isEmpty()) {
            return 0.0;
        }
        int Digitcount = 0;

        for (char c : hostname.toCharArray()) {
            if (Character.isDigit(c)) {
                Digitcount++;
            }

        }
        return (double) Digitcount / hostname.length();

    }

    private static final Set<String> PHISH_KEYWORDS = new HashSet<>(Arrays.asList(
            "access",
            "accounts",
            "auth",
            "security",
            "portal",
            "user",
            "company",
            "admin",
            "credential",
            "identity",
            "login",
            "password",
            "privilege",
            "token",
            "validation",
            "assurance",
            "availability",
            "confidentiality",
            "integrity",
            "privacy",
            "safety",
            "trust",
            "verification",
            "check",
            "key",
            "lock",
            "biometrics",
            "authorize",
            "authentication",
            "session",
            "profile",
            "service",
            "support",
            "notify",
            "email",
            "account",
            "update",
            "secure",
            "notification",
            "transaction",
            "validate",
            "confirmation",
            "manager",
            "assistant",
            "dashboard",
            "information",
            "communication",
            "finance",
            "maintenance",
            "customer",
            "invoice",
            "billing",
            "subscription",
            "order",
            "shipment",
            "purchase",
            "alert",
            "billinginfo",
            "receipt",
            "accountinfo",
            "payment",
            "invoiceinfo",
            "orderinfo"));

    public int phish_hints(String url) {
        if (url == null || url.isEmpty()) {
            return 0;
        }

        String lowerUrl = url.toLowerCase();

        // Split on anything that is NOT a letter or digit
        String[] tokens = lowerUrl.split("[^a-z0-9]+");

        Set<String> foundKeywords = new HashSet<>();

        for (String token : tokens) {
            if (PHISH_KEYWORDS.contains(token)) {
                foundKeywords.add(token);
            }
        }

        return foundKeywords.size();
    }

}
