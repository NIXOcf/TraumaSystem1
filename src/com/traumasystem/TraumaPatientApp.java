package com.traumasystem;

import com.traumasystem.controller.PatientController;
import com.traumasystem.view.MainFrame;

import javax.swing.*;
import java.io.File;

public class TraumaPatientApp {
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize data directory
        String homeDir = System.getProperty("user.home");
        String dataDir = homeDir + File.separator + "TraumaPatientData";

        // Initialize controller
        PatientController controller = new PatientController(dataDir);

        // Start the application GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(controller);
            mainFrame.setVisible(true);
        });
    }
}