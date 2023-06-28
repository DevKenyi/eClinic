package com.example.eclinic001.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patient")


@Data
public class Patient extends User {
    @Transient
    public static String[] VALID_BLOOD_GROUPS = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    @Transient
    public static String[] VALID_GENOTYPE_GROUPS = {"AA", "AS", "SS", "AC"};
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long patientId;

    private String bloodGroup;

    private String genotype;
    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(
           name = "Appointments",
           joinColumns = @JoinColumn(name = "patient_id"),
           inverseJoinColumns = @JoinColumn(name = "doctor_id")
   )
    private Set<Doctor> doctors = new HashSet<>();

    @Transient
    private Collection<Appointments> appointments;
     @Transient
    private Admin admin;
     private String address;









    public void setBloodGroup(String bloodGroup) {
        if (Arrays.asList(VALID_BLOOD_GROUPS).contains(bloodGroup)) {
            this.bloodGroup = bloodGroup;
        }
        throw new IllegalArgumentException("Not a valid Blood group");
    }
    public void setGenotype(String genotype) {
        if (Arrays.asList(VALID_GENOTYPE_GROUPS).contains(genotype)) {
            this.genotype = genotype;
        }
    }

}
