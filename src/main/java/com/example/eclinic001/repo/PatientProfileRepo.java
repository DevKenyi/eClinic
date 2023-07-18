package com.example.eclinic.repo;

import com.example.eclinic.model.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientProfileRepo extends JpaRepository<PatientProfile, Long> {
}
