package com.medilabo.frontend.controller;

import com.medilabo.frontend.dto.NoteRequestDTO;
import com.medilabo.frontend.model.Note;
import com.medilabo.frontend.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Récupère toutes les notes d'un patient (utilisé en AJAX)
     */
    @GetMapping("/patient/{idPatient}")
    @ResponseBody
    public ResponseEntity<List<Note>> getNotesByPatient(@PathVariable Integer idPatient) {
        try {
            List<Note> notes = noteService.getNotesByPatientId(idPatient);
            log.info("Retrieved {} notes for patient {}", notes.size(), idPatient);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            log.error("Error fetching notes for patient {}", idPatient, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Ajoute une nouvelle note
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<Note> createNote(@RequestBody NoteRequestDTO noteRequest) {
        try {
            Note createdNote = noteService.createNote(noteRequest);
            log.info("Created new note for patient: {}", noteRequest.getIdPatient());
            return ResponseEntity.ok(createdNote);
        } catch (Exception e) {
            log.error("Error creating note for patient {}", noteRequest.getIdPatient(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Met à jour une note existante
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Note> updateNote(@PathVariable String id, @RequestBody NoteRequestDTO noteRequest) {
        try {
            Note updatedNote = noteService.updateNote(id, noteRequest);
            log.info("Updated note with id: {}", id);
            return ResponseEntity.ok(updatedNote);
        } catch (Exception e) {
            log.error("Error updating note with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Supprime une note
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        try {
            noteService.deleteNote(id);
            log.info("Deleted note with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting note with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
