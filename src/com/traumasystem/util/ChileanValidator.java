package com.traumasystem.util;

import java.util.regex.Pattern;

public class ChileanValidator {

    /**
     * Validates a Chilean RUT number
     *
     * @param rut The RUT to validate (with or without formatting)
     * @return true if the RUT is valid, false otherwise
     */
    public static boolean validateRut(String rut) {
        if (rut == null || rut.trim().isEmpty()) {
            return false;
        }

        // Remove periods and dashes
        String cleanRut = rut.replace(".", "").replace("-", "").trim().toUpperCase();

        // Basic format check
        if (!Pattern.matches("^\\d{1,8}[0-9K]$", cleanRut)) {
            return false;
        }

        try {
            // Separate the verification digit
            String rutDigits = cleanRut.substring(0, cleanRut.length() - 1);
            char dv = cleanRut.charAt(cleanRut.length() - 1);

            // Calculate the verification digit
            int rutNumber = Integer.parseInt(rutDigits);
            char calculatedDv = calculateVerificationDigit(rutNumber);

            // Compare the calculated verification digit with the provided one
            return dv == calculatedDv;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Calculates the verification digit for a Chilean RUT number
     *
     * @param rut The RUT number without the verification digit
     * @return The verification digit (0-9 or 'K')
     */
    public static char calculateVerificationDigit(int rut) {
        int m = 0;
        int s = 1;

        while (rut > 0) {
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
            rut /= 10;
        }

        return s > 0 ? (char) (s + 47) : 'K';
    }

    /**
     * Formats a Chilean RUT number to the standard format (XX.XXX.XXX-Y)
     *
     * @param rut The RUT to format
     * @return The formatted RUT
     */
    public static String formatRut(String rut) {
        if (rut == null || rut.trim().isEmpty()) {
            return "";
        }

        // Remove any existing formatting
        String cleanRut = rut.replace(".", "").replace("-", "").trim();

        if (cleanRut.length() <= 1) {
            return cleanRut;
        }

        // Extract verification digit
        String verificationDigit = cleanRut.substring(cleanRut.length() - 1);
        String number = cleanRut.substring(0, cleanRut.length() - 1);

        // Format the number with periods
        StringBuilder formatted = new StringBuilder();
        int counter = 0;

        for (int i = number.length() - 1; i >= 0; i--) {
            if (counter == 3) {
                formatted.insert(0, ".");
                counter = 0;
            }
            formatted.insert(0, number.charAt(i));
            counter++;
        }

        // Add the verification digit
        formatted.append("-").append(verificationDigit);

        return formatted.toString();
    }
}
