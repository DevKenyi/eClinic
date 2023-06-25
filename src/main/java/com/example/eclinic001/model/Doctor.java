package com.example.eclinic001.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Doctor extends User {
    @Id
    private Long doctorId;
    @Enumerated(EnumType.STRING)
    private SPECIALIZATION specialization;
    private String qualification;
    private int[] rating = new int[5];
    private boolean availability = true;
    @ManyToMany(mappedBy = "doctors")
    private Set<Patient> patientsSet = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;


}
