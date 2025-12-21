package com.medilabo.frontend.service;

import com.medilabo.frontend.dto.RiskAssessmentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Slf4j
@Service
public class RiskService {

    private final WebClient webClient;

    public RiskService(WebClient webClient) {
        this.webClient = webClient;
    }

    private String getBasicAuthHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            // For simplicity, we're using the same credentials as the frontend
            String password = "user".equals(username) ? "password" : "admin";
            String auth = username + ":" + password;
            return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
        }
        return null;
    }

    public String assessRisk(RiskAssessmentRequest request) {
        log.debug("Requesting risk assessment for patient");
        try {
            String response = webClient.post()
                    .uri("/assess")
                    .header("Authorization", getBasicAuthHeader())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class) // The enum is returned as a string
                    .block();
            
            if (response != null) {
                return response.replace("\"", "");
            }
            return "N/A";
        } catch (Exception e) {
            log.error("Error calling risk assessment service", e);
            return "N/A"; // Return N/A or handle error gracefully
        }
    }
}
