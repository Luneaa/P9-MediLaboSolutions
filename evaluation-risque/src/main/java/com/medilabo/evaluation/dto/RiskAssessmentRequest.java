package com.medilabo.evaluation.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for risk assessment request
 */
@Data
public class RiskAssessmentRequest {

    /**
     * Patient's birth date
     */
    private LocalDate birthDate;

    /**
     * Patient's gender
     */
    private String gender;

    /**
     * Clinical notes
     */
    private List<String> notes;
}
