package com.example.backend.model;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class ModelService {

    private Classifier classifier;
    private Instances structure;

    @PostConstruct
    public void init() {
        try {
            // 1️⃣ Load trained model
            ClassPathResource resource = new ClassPathResource("FYP trained model.model");

            InputStream inputStream = resource.getInputStream();
            classifier = (Classifier) SerializationHelper.read(inputStream);

            System.out.println("Model loaded successfully!");

            // 2️⃣ Build attribute structure EXACTLY like training
            ArrayList<Attribute> attributes = new ArrayList<>();

            attributes.add(new Attribute("ip"));
            attributes.add(new Attribute("nb_at"));
            attributes.add(new Attribute("nb_qm"));
            attributes.add(new Attribute("nb_and"));
            attributes.add(new Attribute("nb_semicolumn"));
            attributes.add(new Attribute("nb_www"));
            attributes.add(new Attribute("ratio_digits_host"));
            attributes.add(new Attribute("tld_in_subdomain"));
            attributes.add(new Attribute("phish_hints"));
            attributes.add(new Attribute("brand_in_subdomain"));

            ArrayList<String> classValues = new ArrayList<>(Arrays.asList("legitimate", "phishing"));
            attributes.add(new Attribute("status", classValues));

            structure = new Instances("url_features", attributes, 0);
            structure.setClassIndex(structure.numAttributes() - 1);

            System.out.println("Model structure created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3️⃣ Prediction method
    public String predict(int ip,
            int nb_at,
            int nb_qm,
            int nb_and,
            int nb_semicolumn,
            int nb_www,
            double ratio_digits_host,
            int tld_in_subdomain,
            int phish_hints,
            int brand_in_subdomain) throws Exception {

        DenseInstance instance = new DenseInstance(structure.numAttributes());

        instance.setDataset(structure);

        // Set values in EXACT same order
        instance.setValue(structure.attribute("ip"), ip);
        instance.setValue(structure.attribute("nb_at"), nb_at);
        instance.setValue(structure.attribute("nb_qm"), nb_qm);
        instance.setValue(structure.attribute("nb_and"), nb_and);
        instance.setValue(structure.attribute("nb_semicolumn"), nb_semicolumn);
        instance.setValue(structure.attribute("nb_www"), nb_www);
        instance.setValue(structure.attribute("ratio_digits_host"), ratio_digits_host);
        instance.setValue(structure.attribute("tld_in_subdomain"), tld_in_subdomain);
        instance.setValue(structure.attribute("phish_hints"), phish_hints);
        instance.setValue(structure.attribute("brand_in_subdomain"), brand_in_subdomain);

        double predictionIndex = classifier.classifyInstance(instance);

        return structure.classAttribute().value((int) predictionIndex);
    }
}
