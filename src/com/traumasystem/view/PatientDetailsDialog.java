package com.traumasystem.view;

import com.traumasystem.controller.PatientController;
import com.traumasystem.model.Patient;
import com.traumasystem.model.SeguimientoRecord;
import com.traumasystem.model.TiempoSeguimiento;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PatientDetailsDialog extends JDialog {
    private final PatientController controller;
    private final Patient patient;

    // Formatter for dates
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PatientDetailsDialog(JFrame parent, PatientController controller, Patient patient) {
        super(parent, "Detalles del Paciente", true);
        this.controller = controller;
        this.patient = patient;

        setSize(800, 600);
        setLocationRelativeTo(parent);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Create tabbed pane for patient info and seguimiento records
        JTabbedPane tabbedPane = new JTabbedPane();

        // Basic patient info panel
        tabbedPane.addTab("Información del Paciente", createPatientInfoPanel());

        // Seguimiento records tabs
        for (TiempoSeguimiento tiempo : TiempoSeguimiento.values()) {
            tabbedPane.addTab(tiempo.getDisplayName(), createSeguimientoPanel(tiempo));
        }

        add(tabbedPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createPatientInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Patient basic info
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add patient info fields
        int row = 0;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = row;
        infoPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(patient.getNombre()), gbc);
        row++;

        // Edad
        gbc.gridx = 0;
        gbc.gridy = row;
        infoPanel.add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(String.valueOf(patient.getEdad())), gbc);
        row++;

        // RUT
        gbc.gridx = 0;
        gbc.gridy = row;
        infoPanel.add(new JLabel("RUT:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(patient.getRut()), gbc);
        row++;

        // Dominancia
        gbc.gridx = 0;
        gbc.gridy = row;
        infoPanel.add(new JLabel("Dominancia:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(patient.getDominancia()), gbc);
        row++;

        // Lesión
        gbc.gridx = 0;
        gbc.gridy = row;
        infoPanel.add(new JLabel("Lesión:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(patient.getLesion()), gbc);
        row++;

        // Delay QX
        gbc.gridx = 0;
        gbc.gridy = row;
        infoPanel.add(new JLabel("Delay QX (días):"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(String.valueOf(patient.getDelayQx())), gbc);
        row++;

        // Fecha Cirugía
        gbc.gridx = 0;
        gbc.gridy = row;
        infoPanel.add(new JLabel("Fecha Cirugía:"), gbc);
        gbc.gridx = 1;
        String fechaCirugia = patient.getFechaCirugia() != null ?
                patient.getFechaCirugia().format(dateFormatter) : "No registrada";
        infoPanel.add(new JLabel(fechaCirugia), gbc);
        row++;

        // Tipo de CX
        gbc.gridx = 0;
        gbc.gridy = row;
        infoPanel.add(new JLabel("Tipo de CX:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(patient.getTipoDeCx()), gbc);

        panel.add(new JScrollPane(infoPanel), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSeguimientoPanel(TiempoSeguimiento tiempo) {
        JPanel panel = new JPanel(new BorderLayout());

        // Get existing record or create a new one
        SeguimientoRecord record = patient.getSeguimientoByTiempo(tiempo);
        boolean isNewRecord = false;

        if (record == null) {
            record = new SeguimientoRecord();
            record.setTiempoSeguimiento(tiempo);
            isNewRecord = true;
        }

        // Record form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Form fields
        final JTextField fechaField = new JTextField(10);
        if (record.getFechaEvaluacion() != null) {
            fechaField.setText(record.getFechaEvaluacion().toString());
        }

        final JTextField qdashField = new JTextField(10);
        qdashField.setText(String.valueOf(record.getQdash()));

        final JTextField prweField = new JTextField(10);
        prweField.setText(String.valueOf(record.getPrwe()));

        final JSpinner evaReposoSpinner = new JSpinner(new SpinnerNumberModel(record.getEvaReposo(), 0, 10, 1));
        final JSpinner evaActividadSpinner = new JSpinner(new SpinnerNumberModel(record.getEvaActividad(), 0, 10, 1));

        final JTextField romField = new JTextField(20);
        romField.setText(record.getRom());

        final JTextField fGripField = new JTextField(10);
        fGripField.setText(String.valueOf(record.getfGrip()));

        final JTextField aperturaSLField = new JTextField(10);
        aperturaSLField.setText(String.valueOf(record.getAperturaSL()));

        final JCheckBox disiCheckbox = new JCheckBox();
        disiCheckbox.setSelected(record.isDisi());

        final JCheckBox subluxCheckbox = new JCheckBox();
        subluxCheckbox.setSelected(record.isSubluxacionDorsalEscafoides());

        final JTextArea observacionesArea = new JTextArea(4, 30);
        observacionesArea.setText(record.getObservaciones());
        observacionesArea.setLineWrap(true);
        observacionesArea.setWrapStyleWord(true);

        // Add fields to form
        int row = 0;

        // Fecha Evaluación
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Fecha Evaluación (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        datePanel.add(fechaField);
        JButton todayButton = new JButton("Hoy");
        todayButton.addActionListener(e -> fechaField.setText(LocalDate.now().toString()));
        datePanel.add(todayButton);
        formPanel.add(datePanel, gbc);
        row++;

        // QDASH
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("QDASH:"), gbc);
        gbc.gridx = 1;
        formPanel.add(qdashField, gbc);
        row++;

        // PRWE
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("PRWE:"), gbc);
        gbc.gridx = 1;
        formPanel.add(prweField, gbc);
        row++;

        // EVA Reposo
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("EVA Reposo (0-10):"), gbc);
        gbc.gridx = 1;
        formPanel.add(evaReposoSpinner, gbc);
        row++;

        // EVA Actividad
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("EVA Actividad (0-10):"), gbc);
        gbc.gridx = 1;
        formPanel.add(evaActividadSpinner, gbc);
        row++;

        // ROM
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("ROM:"), gbc);
        gbc.gridx = 1;
        formPanel.add(romField, gbc);
        row++;

        // F GRIP
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("F GRIP:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fGripField, gbc);
        row++;

        // Apertura SL
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Apertura SL:"), gbc);
        gbc.gridx = 1;
        formPanel.add(aperturaSLField, gbc);
        row++;

        // DISI
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("DISI:"), gbc);
        gbc.gridx = 1;
        formPanel.add(disiCheckbox, gbc);
        row++;

        // Subluxación Dorsal Escafoides
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Subluxación Dorsal Escafoides:"), gbc);
        gbc.gridx = 1;
        formPanel.add(subluxCheckbox, gbc);
        row++;

        // Observaciones
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(observacionesArea), gbc);

        panel.add(new JScrollPane(formPanel), BorderLayout.CENTER);

        // Save button panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Guardar " + tiempo.getDisplayName());

        // Final reference to use in lambda
        final SeguimientoRecord finalRecord = record;
        final boolean finalIsNewRecord = isNewRecord;

        saveButton.addActionListener(e -> {
            try {
                // Parse and validate data
                LocalDate fechaEval = null;
                try {
                    if (!fechaField.getText().trim().isEmpty()) {
                        fechaEval = LocalDate.parse(fechaField.getText().trim());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Formato de fecha inválido. Use YYYY-MM-DD",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double qdash = 0;
                try {
                    if (!qdashField.getText().trim().isEmpty()) {
                        qdash = Double.parseDouble(qdashField.getText().trim());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Valor de QDASH inválido",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double prwe = 0;
                try {
                    if (!prweField.getText().trim().isEmpty()) {
                        prwe = Double.parseDouble(prweField.getText().trim());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Valor de PRWE inválido",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double fGrip = 0;
                try {
                    if (!fGripField.getText().trim().isEmpty()) {
                        fGrip = Double.parseDouble(fGripField.getText().trim());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Valor de F GRIP inválido",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double aperturaSL = 0;
                try {
                    if (!aperturaSLField.getText().trim().isEmpty()) {
                        aperturaSL = Double.parseDouble(aperturaSLField.getText().trim());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Valor de Apertura SL inválido",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update record
                finalRecord.setFechaEvaluacion(fechaEval);
                finalRecord.setQdash(qdash);
                finalRecord.setPrwe(prwe);
                finalRecord.setEvaReposo((int) evaReposoSpinner.getValue());
                finalRecord.setEvaActividad((int) evaActividadSpinner.getValue());
                finalRecord.setRom(romField.getText().trim());
                finalRecord.setfGrip(fGrip);
                finalRecord.setAperturaSL(aperturaSL);
                finalRecord.setDisi(disiCheckbox.isSelected());
                finalRecord.setSubluxacionDorsalEscafoides(subluxCheckbox.isSelected());
                finalRecord.setObservaciones(observacionesArea.getText().trim());

                // Save to patient
                if (finalIsNewRecord) {
                    patient.addSeguimientoRecord(finalRecord);
                }

                controller.updatePatient(patient);

                JOptionPane.showMessageDialog(this,
                        "Seguimiento guardado correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar el seguimiento: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(saveButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
}