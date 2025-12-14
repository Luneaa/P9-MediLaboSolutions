package com.medilabo.notes.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "notes")
public class Note {

    @Id
    private String id;

    private Integer idPatient;

    private Integer idMedecin;

    private String contenu;

    private LocalDateTime date;

    public Note(String id) {
        this.id = id;
    }
}
