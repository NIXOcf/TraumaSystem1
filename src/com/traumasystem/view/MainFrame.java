package com.traumasystem.view;

import com.traumasystem.controller.PatientController;
import com.traumasystem.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame {
    private final PatientController controller;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public MainFrame(PatientController controller) {
        this.controller = controller;

        setTitle("Sistema de Seguimiento de Pacientes de Trauma");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadPatients();
    }

    private void initComponents() {
        // Main layout
        setLayout(new BorderLayout());

        // North panel with search and add buttons
        JPanel northPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(e -> searchPatients());
        searchPanel.add(new JLabel("Buscar: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        northPanel.add(searchPanel, BorderLayout.WEST);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Nuevo Paciente");
        addButton.addActionListener(e -> showAddPatientDialog());
        actionPanel.add(addButton);
        northPanel.add(actionPanel, BorderLayout.EAST);

        add(northPanel, BorderLayout.NORTH);

        // Table model and table
        String[] columnNames = {"Nombre", "Edad", "RUT", "Lesión", "Fecha Cirugía", "Tipo de Cx"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.getTableHeader().setReorderingAllowed(false);

        // Add double-click listener to open patient details
        patientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = patientTable.getSelectedRow();
                    if (row >= 0) {
                        openPatientDetails(row);
                    }
                }
            }
        });

        // Center panel with table
        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);

        // South panel with buttons
        JPanel southPanel = new JPanel();
        JButton viewButton = new JButton("Ver Detalles");
        viewButton.addActionListener(e -> {
            int row = patientTable.getSelectedRow();
            if (row >= 0) {
                openPatientDetails(row);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Por favor seleccione un paciente",
                        "Ningún paciente seleccionado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton editButton = new JButton("Editar");
        editButton.addActionListener(e -> {
            int row = patientTable.getSelectedRow();
            if (row >= 0) {
                editPatient(row);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Por favor seleccione un paciente",
                        "Ningún paciente seleccionado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        southPanel.add(viewButton);
        southPanel.add(editButton);
        add(southPanel, BorderLayout.SOUTH);
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(e -> {
            int row = patientTable.getSelectedRow();
            if (row >= 0) {
                deletePatient(row);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Por favor seleccione un paciente",
                        "Ningún paciente seleccionado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        southPanel.add(viewButton);
        southPanel.add(editButton);
        southPanel.add(deleteButton); // Añadido el botón de eliminar
    }
    private void deletePatient(int row) {
        try {
            List<Patient> patients = controller.getAllPatients();
            if (row < patients.size()) {
                Patient patient = patients.get(row);

                // Pedir confirmación
                int option = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea eliminar el paciente " + patient.getNombre() + "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    boolean success = controller.deletePatient(patient);
                    if (success) {
                        JOptionPane.showMessageDialog(this,
                                "Paciente eliminado correctamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Recargar la lista de pacientes
                        loadPatients();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "No se pudo eliminar el paciente",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar el paciente: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadPatients() {
        try {
            List<Patient> patients = controller.getAllPatients();
            updatePatientTable(patients);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los pacientes: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePatientTable(List<Patient> patients) {
        // Clear the table
        tableModel.setRowCount(0);

        // Add patients to table
        for (Patient patient : patients) {
            Object[] row = {
                    patient.getNombre(),
                    patient.getEdad(),
                    patient.getRut(),
                    patient.getLesion(),
                    patient.getFechaCirugia(),
                    patient.getTipoDeCx()
            };
            tableModel.addRow(row);
        }
    }

    private void searchPatients() {
        String searchTerm = searchField.getText().trim();
        try {
            List<Patient> patients;
            if (searchTerm.isEmpty()) {
                patients = controller.getAllPatients();
            } else {
                patients = controller.searchPatients(searchTerm);
            }
            updatePatientTable(patients);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar pacientes: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddPatientDialog() {
        PatientFormDialog dialog = new PatientFormDialog(this, controller);
        dialog.setVisible(true);

        // Refresh the table after adding a patient
        loadPatients();
    }

    private void openPatientDetails(int row) {
        try {
            List<Patient> patients = controller.getAllPatients();
            if (row < patients.size()) {
                Patient patient = patients.get(row);
                PatientDetailsDialog dialog = new PatientDetailsDialog(this, controller, patient);
                dialog.setVisible(true);

                // Refresh the table after potential updates
                loadPatients();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los detalles del paciente: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editPatient(int row) {
        try {
            List<Patient> patients = controller.getAllPatients();
            if (row < patients.size()) {
                Patient patient = patients.get(row);
                PatientFormDialog dialog = new PatientFormDialog(this, controller, patient);
                dialog.setVisible(true);

                // Refresh the table after editing
                loadPatients();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al editar el paciente: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
