package com.example.backend.Rassa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class extract {

    public static void evaluate(RasaService rasaService) throws Exception {

        // Ground truth: URLs you expect to be extracted
        List<String> expectedUrls = List.of(
                "http://br-ofertasimperdiveis.epizy.com/produto.php?linkcompleto=iphone-6-plus-apple-64gb-cinza-espacial-tela-5-5-retina-4g-camera-8mp-frontal-ios-10-proc.-m8/p/2116558/te/ipho/&id=10",
                "https://semana-da-oferta.com/produtos.php?id=5abad0c01d149",
                "https://scrid-apps-creacust-sslhide90766752024.cread-squi.com/hider_reo/",
                "http://my-softbank-security.com/wap_login.htm",
                "http://www.my-softbank-security.com/wap_login.htm",
                "http://diadesaldaolu.infomando.com",
                "https://sites.google.com/site/helpsettingsrecoveryfbus2018/",
                "http://protvinowifi.ru/",
                "http://socset222.96.lt/",
                "https://help78.000webhostapp.com/log.php");

        // Actual extracted URLs from your system
        List<String> extractedUrls = rasaService.extractAllUrls();

        // Convert both to sets for comparison
        Set<String> expectedSet = new HashSet<>(expectedUrls);
        Set<String> extractedSet = new HashSet<>(extractedUrls);

        // True Positives: correctly extracted URLs
        Set<String> truePositives = new HashSet<>(extractedSet);
        truePositives.retainAll(expectedSet);

        // False Positives: extracted but not expected
        Set<String> falsePositives = new HashSet<>(extractedSet);
        falsePositives.removeAll(expectedSet);

        // False Negatives: expected but not extracted
        Set<String> falseNegatives = new HashSet<>(expectedSet);
        falseNegatives.removeAll(extractedSet);

        int tp = truePositives.size();
        int fp = falsePositives.size();
        int fn = falseNegatives.size();

        double precision = (tp + fp) > 0 ? (double) tp / (tp + fp) : 0.0;
        double recall = (tp + fn) > 0 ? (double) tp / (tp + fn) : 0.0;

        System.out.println("=== Extraction Evaluation ===");
        System.out.println("Expected URLs: " + expectedSet.size());
        System.out.println("Extracted URLs: " + extractedSet.size());
        System.out.println("True Positives: " + tp);
        System.out.println("False Positives: " + fp);
        System.out.println("False Negatives: " + fn);
        System.out.println("Precision: " + String.format("%.2f", precision));
        System.out.println("Recall: " + String.format("%.2f", recall));

        System.out.println("\nCorrectly Extracted URLs:");
        truePositives.forEach(System.out::println);

        System.out.println("\nFalse Positives:");
        falsePositives.forEach(System.out::println);

        System.out.println("\nMissed URLs:");
        falseNegatives.forEach(System.out::println);
    }
}