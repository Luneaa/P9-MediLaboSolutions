package com.medilabo.evaluation.model;

public enum RiskLevel {
    NONE("None"),
    BORDERLINE("Borderline"),
    IN_DANGER("In Danger"),
    EARLY_ONSET("Early Onset");

    private final String label;

    RiskLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
