package com.medilabo.notes.service;

import com.medilabo.notes.dto.NoteRequestDTO;
import com.medilabo.notes.dto.NoteResponseDTO;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.repository.NoteRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Récupère toutes les notes
     * @return Liste de toutes les notes
     */
    @Cacheable("notes")
    public List<NoteResponseDTO> getAllNotes() {
        return noteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une note par son ID
     * @param id L'identifiant de la note
     * @return La note trouvée
     * @throws ResponseStatusException si la note n'existe pas
     */
    @Cacheable(value = "note", key = "#id")
    public NoteResponseDTO getNoteById(String id) {
        Note note = findNoteById(id);
        return convertToDTO(note);
    }

    /**
     * Récupère toutes les notes d'un patient
     * @param idPatient L'identifiant du patient
     * @return Liste des notes du patient
     */
    @Cacheable(value = "patient_notes", key = "#idPatient")
    public List<NoteResponseDTO> getNotesByPatientId(Integer idPatient) {
        return noteRepository.findByIdPatient(idPatient).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Ajoute une nouvelle note
     * @param noteRequest Les données de la note à ajouter
     * @return La note ajoutée
     */
    @CacheEvict(value = {"notes", "note", "patient_notes"}, allEntries = true)
    public NoteResponseDTO addNote(NoteRequestDTO noteRequest) {
        Note note = new Note();
        note.setIdPatient(noteRequest.getIdPatient());
        note.setIdMedecin(noteRequest.getIdMedecin());
        note.setContenu(noteRequest.getContenu());
        note.setDate(LocalDateTime.now());
        
        Note savedNote = noteRepository.save(note);
        return convertToDTO(savedNote);
    }

    /**
     * Met à jour une note existante
     * @param id L'identifiant de la note à modifier
     * @param noteRequest Les nouvelles données de la note
     * @return La note mise à jour
     * @throws ResponseStatusException si la note n'existe pas
     */
    @CacheEvict(value = {"notes", "note", "patient_notes"}, allEntries = true)
    public NoteResponseDTO updateNote(String id, NoteRequestDTO noteRequest) {
        // Vérifier que la note existe
        Note existingNote = findNoteById(id);
        
        // Mettre à jour les champs
        existingNote.setIdPatient(noteRequest.getIdPatient());
        existingNote.setIdMedecin(noteRequest.getIdMedecin());
        existingNote.setContenu(noteRequest.getContenu());
        // La date reste inchangée lors d'une mise à jour
        
        Note updatedNote = noteRepository.save(existingNote);
        return convertToDTO(updatedNote);
    }

    /**
     * Supprime une note
     * @param id L'identifiant de la note à supprimer
     * @throws ResponseStatusException si la note n'existe pas
     */
    @CacheEvict(value = {"notes", "note", "patient_notes"}, allEntries = true)
    public void deleteNote(String id) {
        // Vérifier que la note existe
        Note note = findNoteById(id);
        noteRepository.delete(note);
    }

    /**
     * Trouve une note par son ID (méthode privée)
     * @param id L'identifiant de la note
     * @return La note trouvée
     * @throws ResponseStatusException si la note n'existe pas
     */
    private Note findNoteById(String id) {
        var note = noteRepository.findById(id).orElse(null);

        if (note == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found with id " + id);
        }

        return note;
    }

    /**
     * Convertit une entité Note en DTO
     * @param note L'entité Note
     * @return Le DTO NoteResponseDTO
     */
    private NoteResponseDTO convertToDTO(Note note) {
        NoteResponseDTO dto = new NoteResponseDTO();
        dto.setId(note.getId());
        dto.setIdPatient(note.getIdPatient());
        dto.setIdMedecin(note.getIdMedecin());
        dto.setContenu(note.getContenu());
        dto.setDate(note.getDate());
        return dto;
    }
}
