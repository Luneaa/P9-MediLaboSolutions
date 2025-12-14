package com.medilabo.notes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse contenant les informations d'une note
 */
@Getter
@Setter
@NoArgsConstructor
public class NoteResponseDTO {

    private String id;

    private Integer idPatient;

    private Integer idMedecin;

    private String contenu;

    private LocalDateTime date;
}
