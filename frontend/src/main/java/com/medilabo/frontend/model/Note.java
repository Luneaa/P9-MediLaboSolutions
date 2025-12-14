package com.medilabo.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private String id;
    private Integer idPatient;
    private Integer idMedecin;
    private String contenu;
    private LocalDateTime date;
}
