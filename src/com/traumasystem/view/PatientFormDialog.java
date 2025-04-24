package com.traumasystem.view;

import com.traumasystem.controller.PatientController;
import com.traumasystem.model.Patient;
import com.traumasystem.util.ChileanValidator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;

public class PatientFormDialog extends JDialog {
    private final PatientController controller;
    private final Patient patientToEdit;

    // Form fields
    private JTextField nombreField;
    private JSpinner edadSpinner;
    private JTextField rutField;
    private JComboBox<String> dominanciaCombo;
    private JTextField lesionField;
    private JSpinner delayQxSpinner;
    private JTextField fechaCirugiaField;
    private JTextField tipoDeCxField;

    public PatientFormDialog(JFrame parent, PatientController controller) {
        this(parent, controller, null);
    }

    public PatientFormDialog(JFrame parent, PatientController controller, Patient patient) {
        super(parent, patient == null ? "Nuevo Paciente" : "Editar Paciente", true);
        this.controller = controller;
        this.patientToEdit = patient;

        initComponents();

        // If editing, populate fields
        if (patient != null) {
            populateFields(patient);
        }

        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Create form fields
        nombreField = new JTextField(20);
        edadSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 120, 1));
        rutField = new JTextField(12);
        dominanciaCombo = new JComboBox<>(new String[]{"Diestro", "Zurdo", "Ambidiestro"});
        lesionField = new JTextField(20);
        delayQxSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 365, 1));
        fechaCirugiaField = new JTextField(10);
        tipoDeCxField = new JTextField(20);

        // Add fields to panel
        int row = 0;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);
        row++;

        // Edad
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1;
        formPanel.add(edadSpinner, gbc);
        row++;

        // RUT
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("RUT:"), gbc);
        gbc.gridx = 1;
        JPanel rutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rutPanel.add(rutField);
        JButton formatRutButton = new JButton("Formatear");
        formatRutButton.addActionListener(e -> {
            String rut = rutField.getText().trim();
            rutField.setText(ChileanValidator.formatRut(rut));
        });
        rutPanel.add(formatRutButton);
        formPanel.add(rutPanel, gbc);
        row++;

        // Dominancia
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Dominancia:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dominanciaCombo, gbc);
        row++;

        // Lesión
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Lesión:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lesionField, gbc);
        row++;

        // Delay QX
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Delay QX (días):"), gbc);
        gbc.gridx = 1;
        formPanel.add(delayQxSpinner, gbc);
        row++;

        // Fecha Cirugia
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Fecha Cirugía (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        datePanel.add(fechaCirugiaField);
        JButton todayButton = new JButton("Hoy");
        todayButton.addActionListener(e -> {
            fechaCirugiaField.setText(LocalDate.now().toString());
        });
        datePanel.add(todayButton);
        formPanel.add(datePanel, gbc);
        row++;

        // Tipo de CX
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Tipo de CX:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tipoDeCxField, gbc);

        // Add form panel to dialog
        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> savePatient());
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateFields(Patient patient) {
        nombreField.setText(patient.getNombre());
        edadSpinner.setValue(patient.getEdad());
        rutField.setText(patient.getRut());
        dominanciaCombo.setSelectedItem(patient.getDominancia());
        lesionField.setText(patient.getLesion());
        delayQxSpinner.setValue(patient.getDelayQx());

        if (patient.getFechaCirugia() != null) {
            fechaCirugiaField.setText(patient.getFechaCirugia().toString());
        }

        tipoDeCxField.setText(patient.getTipoDeCx());
    }

    private void savePatient() {
        // Validate fields
        if (nombreField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ChileanValidator.validateRut(rutField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "RUT inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse date
        LocalDate fechaCirugia = null;
        try {
            if (!fechaCirugiaField.getText().trim().isEmpty()) {
                fechaCirugia = LocalDate.parse(fechaCirugiaField.getText().trim());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use YYYY-MM-DD",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (patientToEdit == null) {
                // Create new patient
                controller.createPatient(
                        nombreField.getText().trim(),
                        (int) edadSpinner.getValue(),
                        ChileanValidator.formatRut(rutField.getText().trim()),
                        (String) dominanciaCombo.getSelectedItem(),
                        lesionField.getText().trim(),
                        (int) delayQxSpinner.getValue(),
                        fechaCirugia,
                        tipoDeCxField.getText().trim()
                );
            } else {
                // Update existing patient
                patientToEdit.setNombre(nombreField.getText().trim());
                patientToEdit.setEdad((int) edadSpinner.getValue());
                patientToEdit.setRut(ChileanValidator.formatRut(rutField.getText().trim()));
                patientToEdit.setDominancia((String) dominanciaCombo.getSelectedItem());
                patientToEdit.setLesion(lesionField.getText().trim());
                patientToEdit.setDelayQx((int) delayQxSpinner.getValue());
                patientToEdit.setFechaCirugia(fechaCirugia);
                patientToEdit.setTipoDeCx(tipoDeCxField.getText().trim());

                controller.updatePatient(patientToEdit);
            }

            // Close dialog
            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar paciente: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}