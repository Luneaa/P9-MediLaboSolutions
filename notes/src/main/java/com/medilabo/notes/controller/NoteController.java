package com.medilabo.notes.controller;

import com.medilabo.notes.dto.NoteRequestDTO;
import com.medilabo.notes.dto.NoteResponseDTO;
import com.medilabo.notes.service.NoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Récupère toutes les notes
     * @return Liste de toutes les notes
     */
    @GetMapping
    public List<NoteResponseDTO> getAllNotes() {
        return noteService.getAllNotes();
    }

    /**
     * Récupère une note par son ID
     * @param id L'identifiant de la note
     * @return La note trouvée
     */
    @GetMapping("/{id}")
    public NoteResponseDTO getNote(@PathVariable("id") String id) {
        return noteService.getNoteById(id);
    }

    /**
     * Récupère toutes les notes d'un patient
     * @param idPatient L'identifiant du patient
     * @return Liste des notes du patient
     */
    @GetMapping("/patient/{idPatient}")
    public List<NoteResponseDTO> getNotesByPatient(@PathVariable("idPatient") Integer idPatient) {
        return noteService.getNotesByPatientId(idPatient);
    }

    /**
     * Ajoute une nouvelle note
     * @param noteRequest Les données de la note à ajouter
     * @return La note ajoutée
     */
    @PostMapping
    public NoteResponseDTO createNote(@RequestBody NoteRequestDTO noteRequest) {
        return noteService.addNote(noteRequest);
    }

    /**
     * Met à jour une note existante
     * @param id L'identifiant de la note à modifier
     * @param noteRequest Les nouvelles données de la note
     * @return La note mise à jour
     */
    @PutMapping("/{id}")
    public NoteResponseDTO updateNote(@PathVariable("id") String id, @RequestBody NoteRequestDTO noteRequest) {
        return noteService.updateNote(id, noteRequest);
    }

    /**
     * Supprime une note
     * @param id L'identifiant de la note à supprimer
     */
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable("id") String id) {
        noteService.deleteNote(id);
    }
}
