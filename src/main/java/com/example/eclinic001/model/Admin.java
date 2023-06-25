package com.example.eclinic001.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends User {
    @Id
    private Long adminId;
    @OneToMany
    private Set<Patient> patient = new HashSet<>();
    @OneToMany
    private Set <Doctor> doctor = new HashSet<>();

}
