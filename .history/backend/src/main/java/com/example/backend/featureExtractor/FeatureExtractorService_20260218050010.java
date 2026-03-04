package com.example.backend.featureExtractor;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.IDN;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FeatureExtractorService {

    // -----------------------------
    // Dataset-style constants
    // -----------------------------

    // Matches dataset's having_ip_address() regex (IPv4/, hex IPv4/, IPv6-ish)
    private static final Pattern IP_PATTERN = Pattern.compile(
            "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                    + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\/)|"
                    + "((0x[0-9a-fA-F]{1,2})\\.(0x[0-9a-fA-F]{1,2})\\.(0x[0-9a-fA-F]{1,2})\\.(0x[0-9a-fA-F]{1,2})\\/)|"
                    + "(?:[a-fA-F0-9]{1,4}:){7}[a-fA-F0-9]{1,4}|"
                    + "[0-9a-fA-F]{7}");

    // Dataset's HINTS list (from url_features.py)
    private static final List<String> PHISH_HINTS = List.of(
            "wp", "login", "includes", "admin", "content", "site",
            "images", "js", "alibaba", "css", "myaccount", "dropbox",
            "themes", "plugins", "signin", "view");

    // Same delimiter idea used by dataset raw_words split:
    // re.split(r'[-./?=@&%: _]', ... )
    private static final Pattern RAW_WORD_SPLIT = Pattern.compile("[-\\./\\?=@&%:_\\s]+");

    // Optional: load big brand list like dataset (allbrands.txt)
    // Put it in: src/main/resources/allbrands.txt (one brand per line)
    private final Set<String> allBrands = new HashSet<>();

    @PostConstruct
    public void loadBrandsIfPresent() {
        try {
            ClassPathResource resource = new ClassPathResource("allbrands.txt");
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim().toLowerCase();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        allBrands.add(line);
                    }
                }
            }
            System.out.println("Loaded brands: " + allBrands.size());
        } catch (Exception e) {
            // If you don't add allbrands.txt, fallback to a small set (still works, less
            // accurate)
            allBrands.addAll(List.of(
                    "paypal", "apple", "amazon", "microsoft", "google", "facebook", "netflix",
                    "instagram", "whatsapp", "telegram", "outlook", "office", "onedrive",
                    "dhl", "fedex", "ups", "royalmail", "hsbc", "barclays", "lloyds"));
            System.out.println("allbrands.txt not found; using fallback brands: " + allBrands.size());
        }
    }

    // -----------------------------
    // Helpers (domain parsing)
    // -----------------------------

    private static class DomainParts {
        final String host; // full host
        final String suffix; // tld/public suffix approximation (e.g., com, co.uk)
        final String domain; // registered domain label (e.g., google)
        final String subdomain; // subdomain part only (e.g., "mail" or "a.b")

        DomainParts(String host, String suffix, String domain, String subdomain) {
            this.host = host;
            this.suffix = suffix;
            this.domain = domain;
            this.subdomain = subdomain;
        }
    }

    // NOTE: Python dataset uses tldextract (best). Here is a practical
    // approximation.
    private DomainParts parseDomainParts(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            return new DomainParts("", "", "", "");
        }

        host = IDN.toUnicode(host.toLowerCase(Locale.ROOT));
        String[] labels = host.split("\\.");
        if (labels.length < 2) {
            return new DomainParts(host, "", labels[0], "");
        }

        // Common multi-part public suffixes (extend if needed)
        Set<String> multiSuffixes = Set.of(
                "co.uk", "org.uk", "ac.uk", "gov.uk",
                "com.au", "net.au", "org.au",
                "co.jp", "co.in", "com.br", "com.cn");

        String last = labels[labels.length - 1];
        String last2 = labels[labels.length - 2] + "." + labels[labels.length - 1];

        String suffix;
        int suffixLabelsCount;
        if (labels.length >= 3 && multiSuffixes.contains(last2)) {
            suffix = last2;
            suffixLabelsCount = 2;
        } else {
            suffix = last;
            suffixLabelsCount = 1;
        }

        int domainIndex = labels.length - suffixLabelsCount - 1;
        if (domainIndex < 0) {
            return new DomainParts(host, suffix, "", "");
        }

        String domain = labels[domainIndex];

        String subdomain = "";
        if (domainIndex > 0) {
            subdomain = String.join(".", Arrays.copyOfRange(labels, 0, domainIndex));
        }

        return new DomainParts(host, suffix, domain, subdomain);
    }

    private static int countChar(String s, char target) {
        int count = 0;
        for (char c : s.toCharArray())
            if (c == target)
                count++;
        return count;
    }

    private static int countOccurrences(String haystack, String needle) {
        if (haystack == null || needle == null || needle.isEmpty())
            return 0;
        int count = 0;
        int idx = 0;
        while ((idx = haystack.indexOf(needle, idx)) != -1) {
            count++;
            idx += needle.length();
        }
        return count;
    }

    private static List<String> rawWords(String host, String path, String subdomain) {
        String combined = (host == null ? "" : host) + " " + (path == null ? "" : path) + " "
                + (subdomain == null ? "" : subdomain);
        String[] parts = RAW_WORD_SPLIT.split(combined.toLowerCase(Locale.ROOT));
        List<String> out = new ArrayList<>();
        for (String p : parts) {
            if (!p.isBlank())
                out.add(p);
        }
        return out;
    }

    // -----------------------------
    // YOUR 10 FEATURES (dataset-matching)
    // -----------------------------

    // 1) ip
    public int ip(String url) {
        if (url == null)
            return 0;
        Matcher m = IP_PATTERN.matcher(url);
        return m.find() ? 1 : 0;
    }

    // 2) nb_at
    public int nb_at(String url) {
        return (url == null) ? 0 : countChar(url, '@');
    }

    // 3) nb_qm
    public int nb_qm(String url) {
        return (url == null) ? 0 : countChar(url, '?');
    }

    // 4) nb_and
    public int nb_and(String url) {
        return (url == null) ? 0 : countChar(url, '&');
    }

    // 5) nb_semicolumn
    public int nb_semicolumn(String url) {
        return (url == null) ? 0 : countChar(url, ';');
    }

    // 6) nb_www (dataset counts "www" token in words_raw, NOT substring count)
    public int nb_www(String url) throws URISyntaxException {
        URI uri = new URI(url);
        DomainParts parts = parseDomainParts(url);
        String path = uri.getPath();

        List<String> tokens = rawWords(parts.host, path, parts.subdomain);
        int count = 0;
        for (String t : tokens) {
            if ("www".equals(t))
                count++;
        }
        return count;
    }

    // 7) ratio_digits_host
    public double ratio_digits_host(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String host = uri.getHost();
        if (host == null || host.isBlank())
            return 0.0;

        host = IDN.toUnicode(host.toLowerCase(Locale.ROOT));
        int digits = 0;
        for (char c : host.toCharArray()) {
            if (Character.isDigit(c))
                digits++;
        }
        return (double) digits / host.length();
    }

    // 8) tld_in_subdomain (dataset: return 1 if subdomain contains tld string)
    public int tld_in_subdomain(String url) throws URISyntaxException {
        DomainParts p = parseDomainParts(url);
        if (p.subdomain == null || p.subdomain.isBlank())
            return 0;
        if (p.suffix == null || p.suffix.isBlank())
            return 0;

        return p.subdomain.contains(p.suffix) ? 1 : 0;
    }

    // 9) phish_hints (dataset: count occurrences of hints in URL PATH)
    public int phish_hints(String url) throws URISyntaxException {
        if (url == null || url.isBlank())
            return 0;

        URI uri = new URI(url);
        String path = uri.getPath();
        if (path == null)
            path = "";

        String lowerPath = path.toLowerCase(Locale.ROOT);

        int count = 0;
        for (String hint : PHISH_HINTS) {
            count += countOccurrences(lowerPath, hint);
        }
        return count;
    }

    // 10) brand_in_subdomain
    // Dataset computes brand_in_subdomain via brand_in_path(domain, subdomain)
    // brand_in_path: if ".brand." is inside the string AND brand is NOT in the
    // registered domain -> 1
    public int brand_in_subdomain(String url) throws URISyntaxException {
        DomainParts p = parseDomainParts(url);

        String sub = (p.subdomain == null) ? "" : p.subdomain.toLowerCase(Locale.ROOT);
        String dom = (p.domain == null) ? "" : p.domain.toLowerCase(Locale.ROOT);

        if (sub.isBlank())
            return 0;

        String padded = "." + sub + ".";
        for (String brand : allBrands) {
            if (brand == null || brand.isBlank())
                continue;
            String b = brand.toLowerCase(Locale.ROOT);

            boolean brandInSub = padded.contains("." + b + ".");
            boolean brandInDomain = dom.contains(b);

            if (brandInSub && !brandInDomain) {
                return 1;
            }
        }
        return 0;
    }
}
