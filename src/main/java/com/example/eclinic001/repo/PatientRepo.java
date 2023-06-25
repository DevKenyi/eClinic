package com.example.eclinic001.repo;

import com.example.eclinic001.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {
    Patient findPatientByEmail(String email);

    @Override
    Optional<Patient> findById(Long id);
}
