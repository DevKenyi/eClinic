package com.example.eclinic001.model;

import com.example.eclinic001.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private String purpose;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;
    @OneToMany(mappedBy = "appointments", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LabTest> labTestList ;
}
