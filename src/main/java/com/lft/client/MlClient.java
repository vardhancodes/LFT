package com.lft.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class MlClient {

    private final RestTemplate restTemplate;
    
    // This grabs the URL from our application.yml file.
    // If it's missing, it defaults to localhost:8000
    @Value("${ml.api.url:http://localhost:8000/predict}")
    private String mlApiUrl;

    public MlClient() {
        // RestTemplate is Spring's standard class for making HTTP requests
        this.restTemplate = new RestTemplate();
    }

    /**
     * Sends features to the Python FastAPI and receives a prediction.
     */
    public MlPrediction getPrediction(Map<String, Double> features) {
        log.info("Sending features to ML model at {}: {}", mlApiUrl, features);

        try {
            // Makes a POST request to Python, sending the features Map as JSON
            ResponseEntity<MlPrediction> response = restTemplate.postForEntity(
                    mlApiUrl, 
                    features, 
                    MlPrediction.class
            );
            
            log.info("Received prediction from ML model: {}", response.getBody());
            return response.getBody();
            
        } catch (RestClientException e) {
            // If Python is down, we don't want the whole Java app to crash.
            // We log the error and return a safe default prediction (HOLD).
            log.error("Failed to reach ML API. Returning safe default. Error: {}", e.getMessage());
            return new MlPrediction("HOLD", 0.0);
        }
    }

    /**
     * A Java Record that maps exactly to the JSON our Python API will return.
     * Expected JSON from Python: {"prediction": "BUY", "confidence": 0.85}
     */
    public record MlPrediction(String prediction, double confidence) {}
}