package com.example.backend.model;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.SerializationHelper;

import java.io.InputStream;

@Service
public class ModelService {

    private Classifier classifier;

    @PostConstruct
    public void loadModel() {
        try {
            ClassPathResource resource = new ClassPathResource("FYP trained model.model");

            InputStream inputStream = resource.getInputStream();

            classifier = (Classifier) SerializationHelper.read(inputStream);

            System.out.println("Model loaded successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Classifier getClassifier() {
        return classifier;
    }
}
