package com.medilabo.evaluation.controller;

import com.medilabo.evaluation.dto.RiskAssessmentRequest;
import com.medilabo.evaluation.model.RiskLevel;
import com.medilabo.evaluation.service.RiskAssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assess")
public class RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;

    public RiskAssessmentController(RiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    @PostMapping
    public ResponseEntity<RiskLevel> assessRisk(@RequestBody RiskAssessmentRequest request) {
        RiskLevel riskLevel = riskAssessmentService.assessRisk(request);
        return ResponseEntity.ok(riskLevel);
    }
}
