package com.example.eclinic001.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Appointments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long appointmentId;

    @Column(name = "appointment_datetime")
    private LocalDateTime appointmentDateTime;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patients;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctors;

    private String purpose;
}
