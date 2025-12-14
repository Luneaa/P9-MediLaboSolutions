package com.medilabo.notes.repository;

import com.medilabo.notes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    
    /**
     * Récupère toutes les notes d'un patient spécifique
     * @param idPatient L'identifiant du patient
     * @return Liste des notes du patient
     */
    List<Note> findByIdPatient(Integer idPatient);
}
