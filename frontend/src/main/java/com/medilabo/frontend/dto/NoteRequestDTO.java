package com.medilabo.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création et la modification d'une note
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequestDTO {
    private Integer idPatient;
    private Integer idMedecin;
    private String contenu;
}
