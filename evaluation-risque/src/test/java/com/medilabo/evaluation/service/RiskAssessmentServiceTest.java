package com.medilabo.evaluation.service;

import com.medilabo.evaluation.dto.RiskAssessmentRequest;
import com.medilabo.evaluation.model.RiskLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RiskAssessmentServiceTest {

    private RiskAssessmentService riskAssessmentService;

    @BeforeEach
    void setUp() {
        riskAssessmentService = new RiskAssessmentService();
    }

    // --- Helper methods ---
    private RiskAssessmentRequest createRequest(int age, String gender, List<String> notes) {
        RiskAssessmentRequest request = new RiskAssessmentRequest();
        request.setBirthDate(LocalDate.now().minusYears(age));
        request.setGender(gender);
        request.setNotes(notes);
        return request;
    }

    private List<String> createNotesWithTriggers(int count) {
        // List of triggers from the service:
        // "Hémoglobine A1C", "Microalbumine", "Taille", "Poids", "Fumeur", "Fumeuse",
        // "Anormal", "Cholestérol", "Vertiges", "Rechute", "Réaction", "Anticorps"
        String[] allTriggers = {
                "Hémoglobine A1C", "Microalbumine", "Taille", "Poids", "Fumeur", "Fumeuse",
                "Anormal", "Cholestérol", "Vertiges", "Rechute", "Réaction", "Anticorps"
        };
        
        if (count > allTriggers.length) {
            throw new IllegalArgumentException("Requested more triggers than available for test");
        }
        
        return Arrays.asList(Arrays.copyOfRange(allTriggers, 0, count));
    }

    // --- Tests ---

    @Test
    void testNone_NoTriggers() {
        RiskAssessmentRequest request = createRequest(35, "M", Collections.emptyList());
        assertEquals(RiskLevel.NONE, riskAssessmentService.assessRisk(request));
    }

    @Test
    void testNone_OneTrigger_Over30() {
        RiskAssessmentRequest request = createRequest(35, "M", createNotesWithTriggers(1));
        assertEquals(RiskLevel.NONE, riskAssessmentService.assessRisk(request));
    }

    @Test
    void testBorderline_Over30_TwoTriggers() {
        RiskAssessmentRequest request = createRequest(35, "M", createNotesWithTriggers(2));
        assertEquals(RiskLevel.BORDERLINE, riskAssessmentService.assessRisk(request));
    }

    @Test
    void testBorderline_Over30_FiveTriggers() {
        RiskAssessmentRequest request = createRequest(35, "F", createNotesWithTriggers(5));
        assertEquals(RiskLevel.BORDERLINE, riskAssessmentService.assessRisk(request));
    }

    @Test
    void testInDanger_Male_Under30_ThreeTriggers() {
        RiskAssessmentRequest request = createRequest(25, "M", createNotesWithTriggers(3));
        assertEquals(RiskLevel.IN_DANGER, riskAssessmentService.assessRisk(request));
    }

    @Test
    void testInDanger_Female_Under30_FourTriggers() {
        RiskAssessmentRequest request = createRequest(25, "F", createNotesWithTriggers(4));
        assertEquals(RiskLevel.IN_DANGER, riskAssessmentService.assessRisk(request));
    }

    @Test
    void testInDanger_Over30_SixTriggers() {
        RiskAssessmentRequest request = createRequest(40, "M", createNotesWithTriggers(6));
        assertEquals(RiskLevel.IN_DANGER, riskAssessmentService.assessRisk(request));
    }
    
    @Test
    void testInDanger_Over30_SevenTriggers() {
        RiskAssessmentRequest request = createRequest(40, "F", createNotesWithTriggers(7));
        assertEquals(RiskLevel.IN_DANGER, riskAssessmentService.assessRisk(request));
    }

    @Test
    void testEarlyOnset_Male_Under30_FiveTriggers() {
        RiskAssessmentRequest request = createRequest(20, "M", createNotesWithTriggers(5));
        assertEquals(RiskLevel.EARLY_ONSET, riskAssessmentService.assessRisk(request));
    }

    @Test
    void testEarlyOnset_Female_Under30_SevenTriggers() {
        RiskAssessmentRequest request = createRequest(20, "F", createNotesWithTriggers(7));
        assertEquals(RiskLevel.EARLY_ONSET, riskAssessmentService.assessRisk(request));
    }

    @Test
    void testEarlyOnset_Over30_EightTriggers() {
        RiskAssessmentRequest request = createRequest(50, "M", createNotesWithTriggers(8));
        assertEquals(RiskLevel.EARLY_ONSET, riskAssessmentService.assessRisk(request));
    }
    
    @Test
    void testCaseInsensitivity() {
        RiskAssessmentRequest request = createRequest(35, "M", Arrays.asList("poids", "TAILLE"));
        assertEquals(RiskLevel.BORDERLINE, riskAssessmentService.assessRisk(request));
    }
    
    @Test
    void testMultipleTriggersInOneNote() {
        // "Poids" and "Taille" in one string -> should count as 2 triggers
        RiskAssessmentRequest request = createRequest(35, "M", Arrays.asList("Le Poids et la Taille sont corrects"));
        assertEquals(RiskLevel.BORDERLINE, riskAssessmentService.assessRisk(request));
    }

    @Test
    void testScenariosFromUser() {
        // TestNone
        List<String> notesNone = Collections.singletonList(
                "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé"
        );
        RiskAssessmentRequest reqNone = createRequest(58, "F", notesNone);
        assertEquals(RiskLevel.NONE, riskAssessmentService.assessRisk(reqNone), "TestNone should be NONE");

        // TestBorderline
        List<String> notesBorderline = Arrays.asList(
                "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement",
                "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale"
        );
        RiskAssessmentRequest reqBorderline = createRequest(80, "M", notesBorderline);
        assertEquals(RiskLevel.BORDERLINE, riskAssessmentService.assessRisk(reqBorderline), "TestBorderline should be BORDERLINE");

        // TestInDanger
        List<String> notesInDanger = Arrays.asList(
                "Le patient déclare qu'il fume depuis peu",
                "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d'apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé"
        );
        RiskAssessmentRequest reqInDanger = createRequest(21, "M", notesInDanger);
        assertEquals(RiskLevel.IN_DANGER, riskAssessmentService.assessRisk(reqInDanger), "TestInDanger should be IN_DANGER");

        // TestEarlyOnset
        List<String> notesEarlyOnset = Arrays.asList(
                "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d'être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments",
                "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps",
                "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé",
                "Taille, Poids, Cholestérol, Vertige et Réaction"
        );
        RiskAssessmentRequest reqEarlyOnset = createRequest(23, "F", notesEarlyOnset);
        assertEquals(RiskLevel.EARLY_ONSET, riskAssessmentService.assessRisk(reqEarlyOnset), "TestEarlyOnset should be EARLY_ONSET");
    }

    @Test
    void testPluralSingularHandling() {
        // "Vertiges" should trigger "Vertige"
        // "Poids" + "Vertiges" -> 2 triggers -> Borderline (Age 35 > 30)
        RiskAssessmentRequest reqPlural = createRequest(35, "M", Arrays.asList("Poids", "Vertiges"));
        assertEquals(RiskLevel.BORDERLINE, riskAssessmentService.assessRisk(reqPlural));

        // "Vertige" should trigger "Vertige"
        RiskAssessmentRequest reqSingular = createRequest(35, "M", Arrays.asList("Poids", "Vertige"));
        assertEquals(RiskLevel.BORDERLINE, riskAssessmentService.assessRisk(reqSingular));

        // "Vertige" and "Vertiges" in same text -> Should count as 1 trigger (Vertige)
        // "Poids" + "Vertige et Vertiges" -> 2 triggers (Poids, Vertige).
        RiskAssessmentRequest reqDouble = createRequest(35, "M", Arrays.asList("Poids", "Vertige et Vertiges"));
        assertEquals(RiskLevel.BORDERLINE, riskAssessmentService.assessRisk(reqDouble));
        
        // "Réactions" should trigger "Réaction"
        // "Rechutes" should trigger "Rechute"
        // "Poids" is a trigger.
        // So "Réactions", "Rechutes", "Poids" -> 3 triggers.
        RiskAssessmentRequest reqPlurals = createRequest(25, "M", Arrays.asList("Réactions", "Rechutes", "Poids"));
        // Male < 30, 3 triggers -> IN_DANGER
        assertEquals(RiskLevel.IN_DANGER, riskAssessmentService.assessRisk(reqPlurals));
    }
}
