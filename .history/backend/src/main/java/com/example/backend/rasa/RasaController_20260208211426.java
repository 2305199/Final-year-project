package com.example.backend.rasa;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rasa")
public class RasaController {

    private final RasaService service;

    public RasaController(RasaService service) {
        this.service = service;
    }

    @GetMapping("/responses")
    public ResponseEntity<?> responses() {
        try {
            return ResponseEntity.ok(service.getResponseTexts());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Failed to read domain.yml",
                    "details", e.getMessage()));
        }
    }

    @GetMapping("/responses/{utterName}")
    public ResponseEntity<?> responseByUtter(@PathVariable String utterName) {
        try {
            List<String> texts = service.getTextsForUtter(utterName);
            if (texts.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Utterance not found"));
            }
            return ResponseEntity.ok(texts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Failed to read domain.yml",
                    "details", e.getMessage()));
        }
    }
}
