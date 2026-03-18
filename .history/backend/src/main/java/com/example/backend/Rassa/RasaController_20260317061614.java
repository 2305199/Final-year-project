package com.example.backend.Rassa;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rasa")
public class RasaController {

    private final RasaService rasaService;

    public RasaController(RasaService rasaService) {
        this.rasaService = rasaService;
    }

    // http://localhost:8080/rasa/responses
    @GetMapping("/responses")
    public ResponseEntity<?> getAllResponses() { // <?> RESPONSE CAN BE ANYTHING NOT ONE THING
        try {
            Map<String, List<String>> responses = rasaService.getResponseTexts(); // GET ALL RESPONSES

            System.out.println("Loaded responses keys: " + responses.keySet());

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Failed to read domain.yml",
                    "exception", e.getClass().getName(),
                    "message", e.getMessage()));
        }
    }

    // http://localhost:8080/rasa/responses/ THE UTTER NAME
    @GetMapping("/responses/{utterName}")
    public ResponseEntity<?> getOneUtterance(@PathVariable String utterName) {
        try {
            List<String> texts = rasaService.getTextsForUtter(utterName); // GET MESSAGES OF ONE UTTER

            if (texts.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "error", "Utterance not found",
                        "utterName", utterName));
            }

            return ResponseEntity.ok(texts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Failed to read domain.yml",
                    "exception", e.getClass().getName(),
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/urls")
    public ResponseEntity<?> getUrls() {
        try {
            List<String> urls = rasaService.extractAllUrls(); // GET ALL URLS FROM RESPONSES
            return ResponseEntity.ok(urls); // RETURN THE URLS
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Failed to read domain.yml",
                    "exception", e.getClass().getName(),
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/extract/evaluate")
    public String evaluateExtraction() {
        try {
            extract.evaluate(rasaService);
            return "Extraction evaluation completed. Check console output.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
