package com.example.eclinic001.model;

public enum SPECIALIZATION {
    Cardiologist("Cardiologist"),
    Dermatologist("Dermatologist"),
    Gastroenterologist("Gastroenterologist"),
    Neurologist("Neurologist"),
    Obstetrician_Gynecologist("Obstetrician/Gynecologist "),
    Oncologist("Oncologist"),

    Surgeon("Orthopedic Surgeon"),
    Pediatrician("Pediatrician"),
    Psychiatrist("Psychiatrist"),
    Radiologist("Radiologist"),

    Anesthesiologist("Anesthesiologist");


        private String specialization;

    SPECIALIZATION(String specialization) {
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }
}
