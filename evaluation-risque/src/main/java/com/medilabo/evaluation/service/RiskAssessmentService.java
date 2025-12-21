package com.medilabo.evaluation.service;

import com.medilabo.evaluation.dto.RiskAssessmentRequest;
import com.medilabo.evaluation.model.RiskLevel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RiskAssessmentService {

    private static final List<String> TRIGGERS = Arrays.asList(
            "Hémoglobine A1C",
            "Microalbumine",
            "Taille",
            "Poids",
            "Fumeur",
            "Fumeuse",
            "Anormal",
            "Cholestérol",
            "Vertige",
            "Rechute",
            "Réaction",
            "Anticorps"
    );

    @Cacheable(value = "risk", key = "#request.hashCode()")
    public RiskLevel assessRisk(RiskAssessmentRequest request) {
        if (request.getBirthDate() == null || request.getGender() == null) {
            return RiskLevel.NONE; // Or throw exception, but safe default
        }

        int age = calculateAge(request.getBirthDate());
        int triggerCount = countTriggers(request.getNotes());
        String gender = request.getGender();

        // Logic based on rules
        
        // None: 0 triggers (Implicitly handled by default return or specific check)
        if (triggerCount == 0) {
            return RiskLevel.NONE;
        }

        // Early Onset (Apparition précoce)
        if (age < 30) {
            if ("M".equalsIgnoreCase(gender) && triggerCount >= 5) {
                return RiskLevel.EARLY_ONSET;
            }
            if ("F".equalsIgnoreCase(gender) && triggerCount >= 7) {
                return RiskLevel.EARLY_ONSET;
            }
        } else { // Age >= 30
            if (triggerCount >= 8) {
                return RiskLevel.EARLY_ONSET;
            }
        }

        // In Danger (Danger)
        if (age < 30) {
            if ("M".equalsIgnoreCase(gender) && triggerCount >= 3) {
                return RiskLevel.IN_DANGER;
            }
            if ("F".equalsIgnoreCase(gender) && triggerCount >= 4) {
                return RiskLevel.IN_DANGER;
            }
        } else { // Age >= 30
            if (triggerCount >= 6) {
                return RiskLevel.IN_DANGER;
            }
        }

        // Borderline (Risque limité)
        if (age >= 30 && triggerCount >= 2) {
             return RiskLevel.BORDERLINE;
        }

        return RiskLevel.NONE;
    }

    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private int countTriggers(List<String> notes) {
        if (notes == null || notes.isEmpty()) {
            return 0;
        }

        // Combine all notes into one content string for easier searching, 
        // or search in each note.
        // The requirement says "Le dossier du patient contient...".
        // We need to count distinct triggers present across all notes.
        
        Set<String> foundTriggers = notes.stream()
                .filter(note -> note != null && !note.isEmpty())
                .flatMap(note -> TRIGGERS.stream()
                        .filter(trigger -> note.toLowerCase().contains(trigger.toLowerCase()))
                )
                .collect(Collectors.toSet());

        return foundTriggers.size();
    }
}
