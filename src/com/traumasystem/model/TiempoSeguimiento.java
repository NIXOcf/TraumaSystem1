package com.traumasystem.model;

public enum TiempoSeguimiento {
    INICIAL("Inicial"),
    TRES_MESES("3 Meses"),
    SEIS_MESES("6 Meses"),
    UN_ANO("1 Año");

    private final String displayName;

    TiempoSeguimiento(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
