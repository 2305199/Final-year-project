package com.example.backend.RASA;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

public class LoadYML {

    public static void main(String[] args) {

        LogConfiguration config;

        Yaml yaml = new Yaml();

        Path domainPath = Path.of(
                "C:/Users/Zia Rahman/Documents/chatbots/rasa/domain.yml");

        try (InputStream in = Files.newInputStream(domainPath)) {
            config = yaml.loadAs(in, LogConfiguration.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        List<Utterance> greetings = config.getResponses().get("utter_greet");

        if (greetings != null) {
            for (Utterance u : greetings) {
                System.out.println(u.getText());
            }
        }
    }
}
