package com.traumasystem.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Patient {
    private String id;                   // Unique identifier
    private String nombre;               // Patient's full name
    private int edad;                    // Age
    private String rut;                  // Chilean RUT number
    private String dominancia;           // Hand dominance (diestro/zurdo)
    private String lesion;               // Injury description
    private int delayQx;                 // Surgical delay in days
    private LocalDate fechaCirugia;      // Surgery date
    private String tipoDeCx;             // Surgery type

    // List of follow-up records (initial, 3 months, 6 months, 1 year)
    private List<SeguimientoRecord> seguimientoRecords;

    public Patient() {
        this.id = UUID.randomUUID().toString();
        this.seguimientoRecords = new ArrayList<>();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getDominancia() {
        return dominancia;
    }

    public void setDominancia(String dominancia) {
        this.dominancia = dominancia;
    }

    public String getLesion() {
        return lesion;
    }

    public void setLesion(String lesion) {
        this.lesion = lesion;
    }

    public int getDelayQx() {
        return delayQx;
    }

    public void setDelayQx(int delayQx) {
        this.delayQx = delayQx;
    }

    public LocalDate getFechaCirugia() {
        return fechaCirugia;
    }

    public void setFechaCirugia(LocalDate fechaCirugia) {
        this.fechaCirugia = fechaCirugia;
    }

    public String getTipoDeCx() {
        return tipoDeCx;
    }

    public void setTipoDeCx(String tipoDeCx) {
        this.tipoDeCx = tipoDeCx;
    }

    public List<SeguimientoRecord> getSeguimientoRecords() {
        return seguimientoRecords;
    }

    public void setSeguimientoRecords(List<SeguimientoRecord> seguimientoRecords) {
        this.seguimientoRecords = seguimientoRecords;
    }

    public void addSeguimientoRecord(SeguimientoRecord record) {
        this.seguimientoRecords.add(record);
    }

    /**
     * Get a seguimiento record by its time point
     */
    public SeguimientoRecord getSeguimientoByTiempo(TiempoSeguimiento tiempo) {
        for (SeguimientoRecord record : seguimientoRecords) {
            if (record.getTiempoSeguimiento() == tiempo) {
                return record;
            }
        }
        return null;
    }
}
