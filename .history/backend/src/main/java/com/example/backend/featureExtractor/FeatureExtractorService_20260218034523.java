package com.example.backend.featureExtractor;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
// Add these imports at the top of FeatureExtractorService
import java.net.IDN;
import java.util.List;

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

        String[] tokens = lowerUrl.split("[^a-z0-9]+");

        Set<String> foundKeywords = new HashSet<>();

        for (String token : tokens) {
            if (PHISH_KEYWORDS.contains(token)) {
                foundKeywords.add(token);
            }
        }

        return foundKeywords.size();
    }

    public int length_url(String url) {
        return url.length();
    }

    public int nb_dots(String url) {
        int count = 0;
        for (char c : url.toCharArray()) {
            if (c == '.') {
                count++;
            }
        }
        return count;
    }

    public int tld_in_subdomain(String URL) throws URISyntaxException {
        URI uri = new URI(URL);
        String host = uri.getHost();

        if (host == null || host.isBlank())
            return 0;

        // Convert punycode to unicode safely (optional)
        host = IDN.toUnicode(host.toLowerCase());

        String[] labels = host.split("\\.");
        if (labels.length < 3)
            return 0; // no subdomain (example.com)

        // subdomain labels = everything before last two labels
        int subEnd = labels.length - 2;

        // Small TLD list (extend if you want)
        // Note: include common 2nd-level ones too: "co", "com", "net", "org"
        List<String> tlds = Arrays.asList(
                "com", "net", "org", "info", "biz", "gov", "edu",
                "co", "uk", "us", "ru", "de", "fr", "it", "es", "nl", "ca", "au", "br", "in", "cn", "jp");

        for (int i = 0; i < subEnd; i++) {
            String label = labels[i];
            if (tlds.contains(label)) {
                return 1;
            }
        }
        return 0;
    }

    private static final Set<String> BRAND_KEYWORDS = new HashSet<>(Arrays.asList(
            "paypal", "apple", "amazon", "microsoft", "google", "facebook", "netflix",
            "instagram", "whatsapp", "telegram", "outlook", "office", "onedrive",
            "dhl", "fedex", "ups", "royalmail", "bank", "hsbc", "barclays", "lloyds"));

    public int brand_in_subdomain(String URL) throws URISyntaxException {
        URI uri = new URI(URL);
        String host = uri.getHost();

        if (host == null || host.isBlank())
            return 0;

        host = IDN.toUnicode(host.toLowerCase());
        String[] labels = host.split("\\.");
        if (labels.length < 3)
            return 0; // no subdomain

        int subEnd = labels.length - 2;

        for (int i = 0; i < subEnd; i++) {
            String label = labels[i];

            // Check exact match OR contains
            for (String brand : BRAND_KEYWORDS) {
                if (label.equals(brand) || label.contains(brand)) {
                    return 1;
                }
            }
        }
        return 0;
    }

}
