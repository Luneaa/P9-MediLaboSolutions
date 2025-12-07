package com.medilabo.patient.service;

import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(int id) {
        var patient = patientRepository.findById(id).orElse(null);

        if (patient == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found with id " + id);
        }

        return patient;
    }

    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(int id, Patient patient) {
        // Verify patient exists
        Patient existingPatient = getPatientById(id);
        
        // Update fields
        existingPatient.setNom(patient.getNom());
        existingPatient.setPrenom(patient.getPrenom());
        existingPatient.setDateNaissance(patient.getDateNaissance());
        existingPatient.setGenre(patient.getGenre());
        existingPatient.setAdresse(patient.getAdresse());
        existingPatient.setTelephone(patient.getTelephone());
        
        return patientRepository.save(existingPatient);
    }

    public void deletePatient(int id) {
        // Verify patient exists
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
    }
}
