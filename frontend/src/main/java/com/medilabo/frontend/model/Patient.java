package com.medilabo.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private Integer id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private Genre genre;
    private String adresse;
    private String telephone;

    public int getAge() {
        if (dateNaissance == null) {
            return 0;
        }
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }
}
