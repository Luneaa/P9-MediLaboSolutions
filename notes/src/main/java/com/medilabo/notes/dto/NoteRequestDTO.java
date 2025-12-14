package com.medilabo.notes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO pour la création et la modification d'une note
 */
@Getter
@Setter
@NoArgsConstructor
public class NoteRequestDTO {

    private Integer idPatient;

    private Integer idMedecin;

    private String contenu;
}
