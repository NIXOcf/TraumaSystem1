package com.traumasystem.controller;

import com.traumasystem.model.Patient;
import com.traumasystem.model.SeguimientoRecord;
import com.traumasystem.model.TiempoSeguimiento;
import com.traumasystem.util.JsonDataManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PatientController {
    private final JsonDataManager dataManager;

    public PatientController(String dataDirectory) {
        this.dataManager = new JsonDataManager(dataDirectory);
    }

    /**
     * Create and save a new patient
     */
    public Patient createPatient(String nombre, int edad, String rut, String dominancia,
                                 String lesion, int delayQx, LocalDate fechaCirugia, String tipoDeCx) throws IOException {
        Patient patient = new Patient();
        patient.setNombre(nombre);
        patient.setEdad(edad);
        patient.setRut(rut);
        patient.setDominancia(dominancia);
        patient.setLesion(lesion);
        patient.setDelayQx(delayQx);
        patient.setFechaCirugia(fechaCirugia);
        patient.setTipoDeCx(tipoDeCx);

        // Create initial empty follow-up record
        SeguimientoRecord initialRecord = new SeguimientoRecord();
        initialRecord.setTiempoSeguimiento(TiempoSeguimiento.INICIAL);
        patient.addSeguimientoRecord(initialRecord);

        dataManager.savePatient(patient);
        return patient;
    }

    /**
     * Add or update a seguimiento record for a patient
     */
    public void saveSeguimientoRecord(Patient patient, SeguimientoRecord record) throws IOException {
        // Check if a record for this time point already exists
        SeguimientoRecord existingRecord = patient.getSeguimientoByTiempo(record.getTiempoSeguimiento());

        if (existingRecord != null) {
            // Update existing record (remove and add new one)
            patient.getSeguimientoRecords().remove(existingRecord);
        }

        patient.addSeguimientoRecord(record);
        dataManager.savePatient(patient);
    }

    /**
     * Update an existing patient
     */
    public void updatePatient(Patient patient) throws IOException {
        dataManager.savePatient(patient);
    }

    /**
     * Get a patient by ID
     */
    public Patient getPatient(String id) throws IOException {
        return dataManager.loadPatient(id);
    }

    /**
     * Get all patients
     */
    public List<Patient> getAllPatients() throws IOException {
        return dataManager.getAllPatients();
    }

    /**
     * Search patients by name or RUT
     */
    public List<Patient> searchPatients(String searchTerm) throws IOException {
        String term = searchTerm.toLowerCase();
        return dataManager.getAllPatients().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(term) ||
                        p.getRut().replace(".", "").replace("-", "").contains(term))
                .collect(Collectors.toList());
    }
    /**
     * Delete a patient by ID
     *
     * @param patientId The ID of the patient to delete
     * @return true if the patient was deleted successfully, false otherwise
     */
    public boolean deletePatient(String patientId) {
        return dataManager.deletePatient(patientId);
    }

    /**
     * Delete a patient
     *
     * @param patient The patient to delete
     * @return true if the patient was deleted successfully, false otherwise
     */
    public boolean deletePatient(Patient patient) {
        return deletePatient(patient.getId());
    }
}
