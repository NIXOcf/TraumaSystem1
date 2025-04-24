package com.traumasystem.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.traumasystem.model.Patient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonDataManager {
    private final String dataDirectory;
    private final Gson gson;

    public JsonDataManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;

        // Create GSON instance with custom type adapter for LocalDate
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        // Ensure data directory exists
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Save a patient to JSON file
     *
     * @param patient The patient to save
     * @throws IOException If an error occurs during file operations
     */
    public void savePatient(Patient patient) throws IOException {
        String fileName = dataDirectory + File.separator + "patient_" + patient.getId() + ".json";
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(patient, writer);
        }
    }

    /**
     * Load a patient from JSON file
     *
     * @param patientId The ID of the patient to load
     * @return The loaded Patient object
     * @throws IOException If an error occurs during file operations
     */
    public Patient loadPatient(String patientId) throws IOException {
        String fileName = dataDirectory + File.separator + "patient_" + patientId + ".json";
        try (FileReader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, Patient.class);
        }
    }

    /**
     * Get a list of all patients
     *
     * @return List of all patients
     */
    public List<Patient> getAllPatients() throws IOException {
        List<Patient> patients = new ArrayList<>();
        File dir = new File(dataDirectory);

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.startsWith("patient_") && name.endsWith(".json"));

            if (files != null) {
                for (File file : files) {
                    try (FileReader reader = new FileReader(file)) {
                        Patient patient = gson.fromJson(reader, Patient.class);
                        patients.add(patient);
                    }
                }
            }
        }

        return patients;
    }

    /**
     * Custom JSON adapter for LocalDate
     */
    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(final LocalDate date, final Type typeOfSrc,
                                     final JsonSerializationContext context) {
            return new JsonPrimitive(date.format(formatter));
        }

        @Override
        public LocalDate deserialize(final JsonElement json, final Type typeOfT,
                                     final JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString(), formatter);
        }
    }
    /**
     * Delete a patient from the file system
     *
     * @param patientId The ID of the patient to delete
     * @return true if the patient was deleted successfully, false otherwise
     */
    public boolean deletePatient(String patientId) {
        String fileName = dataDirectory + File.separator + "patient_" + patientId + ".json";
        File file = new File(fileName);
        return file.exists() && file.delete();
    }
}