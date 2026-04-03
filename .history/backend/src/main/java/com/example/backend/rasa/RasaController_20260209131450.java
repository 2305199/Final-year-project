package com.example.backend.rasa;

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

    // GET http://localhost:8080/rasa/responses
    @GetMapping("/responses")
    public ResponseEntity<?> getAllResponses() {
        try {
            return ResponseEntity.ok(rasaService.getResponseTexts());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Failed to read domain.yml",
                    "exception", e.getClass().getName(),
                    "message", e.getMessage()));
        }
    }

    // GET http://localhost:8080/rasa/responses/utter_greet
    @GetMapping("/responses/{utterName}")
    public ResponseEntity<?> getOneUtter(@PathVariable String utterName) {
        try {
            List<String> texts = rasaService.getTextsForUtter(utterName);
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
}
