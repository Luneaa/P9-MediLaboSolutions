package com.medilabo.frontend.service;

import com.medilabo.frontend.dto.NoteRequestDTO;
import com.medilabo.frontend.model.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class NoteService {

    private final WebClient webClient;

    public NoteService(WebClient webClient) {
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

    public List<Note> getAllNotes() {
        log.debug("Fetching all notes from gateway");
        return webClient.get()
                .uri("/notes")
                .header("Authorization", getBasicAuthHeader())
                .retrieve()
                .bodyToFlux(Note.class)
                .collectList()
                .block();
    }

    public Note getNoteById(String id) {
        log.debug("Fetching note with id: {}", id);
        return webClient.get()
                .uri("/notes/{id}", id)
                .header("Authorization", getBasicAuthHeader())
                .retrieve()
                .bodyToMono(Note.class)
                .block();
    }

    public List<Note> getNotesByPatientId(Integer idPatient) {
        log.debug("Fetching notes for patient with id: {}", idPatient);
        List<Note> notes = webClient.get()
                .uri("/notes/patient/{idPatient}", idPatient)
                .header("Authorization", getBasicAuthHeader())
                .retrieve()
                .bodyToFlux(Note.class)
                .collectList()
                .block();
        
        // Trier les notes par date décroissante (plus récente en premier)
        if (notes != null) {
            notes.sort((n1, n2) -> n2.getDate().compareTo(n1.getDate()));
        }
        
        return notes;
    }

    public Note createNote(NoteRequestDTO noteRequest) {
        log.debug("Creating new note for patient: {}", noteRequest.getIdPatient());
        return webClient.post()
                .uri("/notes")
                .header("Authorization", getBasicAuthHeader())
                .bodyValue(noteRequest)
                .retrieve()
                .bodyToMono(Note.class)
                .block();
    }

    public Note updateNote(String id, NoteRequestDTO noteRequest) {
        log.debug("Updating note with id: {}", id);
        return webClient.put()
                .uri("/notes/{id}", id)
                .header("Authorization", getBasicAuthHeader())
                .bodyValue(noteRequest)
                .retrieve()
                .bodyToMono(Note.class)
                .block();
    }

    public void deleteNote(String id) {
        log.debug("Deleting note with id: {}", id);
        webClient.delete()
                .uri("/notes/{id}", id)
                .header("Authorization", getBasicAuthHeader())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
