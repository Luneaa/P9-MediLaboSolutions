package com.medilabo.frontend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RiskAssessmentRequest {
    private LocalDate birthDate;
    private String gender;
    private List<String> notes;
}
