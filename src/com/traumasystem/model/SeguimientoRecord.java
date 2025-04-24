package com.traumasystem.model;

import java.time.LocalDate;

public class SeguimientoRecord {
    private String id;
    private TiempoSeguimiento tiempoSeguimiento;
    private LocalDate fechaEvaluacion;

    // Required measurements
    private double qdash;
    private double prwe;
    private int evaReposo;
    private int evaActividad;
    private String rom;         // Range of motion
    private double fGrip;       // Grip strength
    private double aperturaSL;  // SL aperture
    private boolean disi;       // DISI present
    private boolean subluxacionDorsalEscafoides; // Dorsal subluxation of the scaphoid

    // Additional notes
    private String observaciones;

    public SeguimientoRecord() {
        this.id = java.util.UUID.randomUUID().toString();
        this.fechaEvaluacion = LocalDate.now();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TiempoSeguimiento getTiempoSeguimiento() {
        return tiempoSeguimiento;
    }

    public void setTiempoSeguimiento(TiempoSeguimiento tiempoSeguimiento) {
        this.tiempoSeguimiento = tiempoSeguimiento;
    }

    public LocalDate getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(LocalDate fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public double getQdash() {
        return qdash;
    }

    public void setQdash(double qdash) {
        this.qdash = qdash;
    }

    public double getPrwe() {
        return prwe;
    }

    public void setPrwe(double prwe) {
        this.prwe = prwe;
    }

    public int getEvaReposo() {
        return evaReposo;
    }

    public void setEvaReposo(int evaReposo) {
        this.evaReposo = evaReposo;
    }

    public int getEvaActividad() {
        return evaActividad;
    }

    public void setEvaActividad(int evaActividad) {
        this.evaActividad = evaActividad;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public double getfGrip() {
        return fGrip;
    }

    public void setfGrip(double fGrip) {
        this.fGrip = fGrip;
    }

    public double getAperturaSL() {
        return aperturaSL;
    }

    public void setAperturaSL(double aperturaSL) {
        this.aperturaSL = aperturaSL;
    }

    public boolean isDisi() {
        return disi;
    }

    public void setDisi(boolean disi) {
        this.disi = disi;
    }

    public boolean isSubluxacionDorsalEscafoides() {
        return subluxacionDorsalEscafoides;
    }

    public void setSubluxacionDorsalEscafoides(boolean subluxacionDorsalEscafoides) {
        this.subluxacionDorsalEscafoides = subluxacionDorsalEscafoides;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
