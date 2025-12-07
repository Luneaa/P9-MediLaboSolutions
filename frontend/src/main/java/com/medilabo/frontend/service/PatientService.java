package com.medilabo.frontend.service;

import com.medilabo.frontend.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class PatientService {

    private final WebClient webClient;

    public PatientService(WebClient webClient) {
        this.webClient = webClient;
    }

    private String getBasicAuthHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            // For simplicity, we're using the same credentials as the frontend
            // In a real app, you'd want to store the password securely or use tokens
            String password = "user".equals(username) ? "password" : "admin";
            String auth = username + ":" + password;
            return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
        }
        return null;
    }

    public List<Patient> getAllPatients() {
        log.debug("Fetching all patients from gateway");
        return webClient.get()
                .uri("/patients")
                .header("Authorization", getBasicAuthHeader())
                .retrieve()
                .bodyToFlux(Patient.class)
                .collectList()
                .block();
    }

    public Patient getPatientById(Integer id) {
        log.debug("Fetching patient with id: {}", id);
        return webClient.get()
                .uri("/patients/{id}", id)
                .header("Authorization", getBasicAuthHeader())
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }

    public Patient createPatient(Patient patient) {
        log.debug("Creating new patient: {} {}", patient.getPrenom(), patient.getNom());
        return webClient.post()
                .uri("/patients")
                .header("Authorization", getBasicAuthHeader())
                .bodyValue(patient)
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }

    public Patient updatePatient(Integer id, Patient patient) {
        log.debug("Updating patient with id: {}", id);
        return webClient.put()
                .uri("/patients/{id}", id)
                .header("Authorization", getBasicAuthHeader())
                .bodyValue(patient)
                .retrieve()
                .bodyToMono(Patient.class)
                .block();
    }

    public void deletePatient(Integer id) {
        log.debug("Deleting patient with id: {}", id);
        webClient.delete()
                .uri("/patients/{id}", id)
                .header("Authorization", getBasicAuthHeader())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
