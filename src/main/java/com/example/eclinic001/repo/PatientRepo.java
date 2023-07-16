package com.example.eclinic001.repo;

import com.example.eclinic001.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {
   // Patient findPatientByEmail(String email);

    @Override
    Optional<Patient> findById(Long id);

    Long findPatientByPatientId(Long id);
    Long findPatientPatientIdByEmail(@RequestParam String email);

    @Query("SELECT p FROM Patient p WHERE p.email = :email")
    Patient findPatientByEmail(@Param("email") String email);



}
