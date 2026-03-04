package com.example.backend.RASA;

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

    @GetMapping("/responses")
    public ResponseEntity<?> getAllResponses() {
        try {
            return ResponseEntity.ok(rasaService.getResponses());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Failed to read domain.yml",
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/responses/{utterName}")
    public ResponseEntity<?> getUtterance(@PathVariable String utterName) {
        try {
            List<String> texts = rasaService.getUtterance(utterName);
            if (texts.isEmpty()) {
                return ResponseEntity.status(404).body("Utterance not found");
            }
            return ResponseEntity.ok(texts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
