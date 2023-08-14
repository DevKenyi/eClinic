package com.example.eclinic001.repo;

import com.example.eclinic001.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorsRepo extends JpaRepository<Doctor, Long> {
    Doctor findDoctorByEmail(String email);

    Doctor getDoctorByEmail(String email);
    Doctor findDoctorByDoctorId(Long id);
}
