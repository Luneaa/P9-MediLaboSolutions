package com.medilabo.frontend.controller;

import com.medilabo.frontend.model.Patient;
import com.medilabo.frontend.service.NoteService;
import com.medilabo.frontend.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final NoteService noteService;

    public PatientController(PatientService patientService, NoteService noteService) {
        this.patientService = patientService;
        this.noteService = noteService;
    }

    @GetMapping
    public String listPatients(Model model) {
        try {
            List<Patient> patients = patientService.getAllPatients();
            model.addAttribute("patients", patients);
            log.info("Retrieved {} patients", patients.size());
        } catch (Exception e) {
            log.error("Error fetching patients", e);
            model.addAttribute("error", "Erreur lors de la récupération des patients");
        }
        return "patients/list";
    }

    @GetMapping("/new")
    public String createPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        log.info("Displaying create patient form");
        return "patients/create";
    }

    @PostMapping("/new")
    public String createPatient(@ModelAttribute Patient patient, Model model) {
        try {
            Patient createdPatient = patientService.createPatient(patient);
            log.info("Created new patient with id: {}", createdPatient.getId());
            return "redirect:/patients/" + createdPatient.getId();
        } catch (Exception e) {
            log.error("Error creating patient", e);
            model.addAttribute("error", "Erreur lors de la création du patient");
            model.addAttribute("patient", patient);
            return "patients/create";
        }
    }

    @GetMapping("/{id}")
    public String patientDetail(@PathVariable Integer id, Model model) {
        try {
            Patient patient = patientService.getPatientById(id);
            model.addAttribute("patient", patient);
            log.info("Retrieved patient with id: {}", id);
            
            // Charger les notes du patient
            try {
                List<com.medilabo.frontend.model.Note> notes = noteService.getNotesByPatientId(id);
                model.addAttribute("notes", notes);
                log.info("Retrieved {} notes for patient {}", notes.size(), id);
            } catch (Exception noteException) {
                log.error("Error fetching notes for patient {}", id, noteException);
                // Continuer même si les notes ne peuvent pas être récupérées
                model.addAttribute("notes", List.of());
            }
        } catch (Exception e) {
            log.error("Error fetching patient with id: {}", id, e);
            model.addAttribute("error", "Erreur lors de la récupération du patient");
        }
        return "patients/detail";
    }

    @GetMapping("/{id}/edit")
    public String editPatientForm(@PathVariable Integer id, Model model) {
        try {
            Patient patient = patientService.getPatientById(id);
            model.addAttribute("patient", patient);
            log.info("Displaying edit form for patient with id: {}", id);
        } catch (Exception e) {
            log.error("Error fetching patient for edit with id: {}", id, e);
            model.addAttribute("error", "Erreur lors de la récupération du patient");
        }
        return "patients/edit";
    }

    @PostMapping("/{id}/edit")
    public String updatePatient(@PathVariable Integer id, @ModelAttribute Patient patient, Model model) {
        try {
            patient.setId(id);
            patientService.updatePatient(id, patient);
            log.info("Updated patient with id: {}", id);
            return "redirect:/patients/" + id;
        } catch (Exception e) {
            log.error("Error updating patient with id: {}", id, e);
            model.addAttribute("error", "Erreur lors de la mise à jour du patient");
            model.addAttribute("patient", patient);
            return "patients/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deletePatient(@PathVariable Integer id) {
        try {
            patientService.deletePatient(id);
            log.info("Deleted patient with id: {}", id);
            return "redirect:/patients";
        } catch (Exception e) {
            log.error("Error deleting patient with id: {}", id, e);
            return "redirect:/patients/" + id + "?error=delete";
        }
    }

    @PostMapping("/{id}/notes")
    public String addNote(@PathVariable Integer id, @ModelAttribute("contenu") String contenu, Model model) {
        try {
            if (contenu == null || contenu.trim().isEmpty()) {
                model.addAttribute("noteError", "Le contenu de la note ne peut pas être vide");
                return "redirect:/patients/" + id;
            }

            com.medilabo.frontend.dto.NoteRequestDTO noteRequest = new com.medilabo.frontend.dto.NoteRequestDTO();
            noteRequest.setIdPatient(id);
            noteRequest.setContenu(contenu.trim());
            
            noteService.createNote(noteRequest);
            log.info("Created note for patient with id: {}", id);
            return "redirect:/patients/" + id;
        } catch (Exception e) {
            log.error("Error creating note for patient with id: {}", id, e);
            model.addAttribute("noteError", "Erreur lors de l'ajout de la note");
            return "redirect:/patients/" + id;
        }
    }

    @PostMapping("/{patientId}/notes/{noteId}/delete")
    public String deleteNote(@PathVariable Integer patientId, @PathVariable String noteId) {
        try {
            noteService.deleteNote(noteId);
            log.info("Deleted note {} for patient {}", noteId, patientId);
            return "redirect:/patients/" + patientId;
        } catch (Exception e) {
            log.error("Error deleting note {} for patient {}", noteId, patientId, e);
            return "redirect:/patients/" + patientId + "?error=deleteNote";
        }
    }

    @PostMapping("/{patientId}/notes/{noteId}/edit")
    public String updateNote(@PathVariable Integer patientId, @PathVariable String noteId, 
                            @ModelAttribute("contenu") String contenu) {
        try {
            if (contenu == null || contenu.trim().isEmpty()) {
                return "redirect:/patients/" + patientId + "?error=emptyNote";
            }

            com.medilabo.frontend.dto.NoteRequestDTO noteRequest = new com.medilabo.frontend.dto.NoteRequestDTO();
            noteRequest.setIdPatient(patientId);
            noteRequest.setContenu(contenu.trim());
            
            noteService.updateNote(noteId, noteRequest);
            log.info("Updated note {} for patient {}", noteId, patientId);
            return "redirect:/patients/" + patientId;
        } catch (Exception e) {
            log.error("Error updating note {} for patient {}", noteId, patientId, e);
            return "redirect:/patients/" + patientId + "?error=updateNote";
        }
    }
}
