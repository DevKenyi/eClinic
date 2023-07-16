package com.example.eclinic001.service;

import com.example.eclinic001.enums.ROLES;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.repo.DoctorsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class DoctorService {
    @Autowired
    private DoctorsRepo repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public ResponseEntity<Doctor> registerDoctor(Doctor doctor) {

        if(repo.findDoctorByEmail(doctor.getEmail())!=null){
            throw new UsernameNotFoundException("This Doctor with "+ doctor.getEmail()+" currently exist");

        }
        else {
            doctor.validatePassword();
            doctor.encodePassword(passwordEncoder);
            Doctor regDoctor = new Doctor();
            regDoctor.setFirstname(doctor.getFirstname());
            regDoctor.setLastname(doctor.getLastname());
            regDoctor.setEmail(doctor.getEmail());
            regDoctor.setAvailability(doctor.isAvailability());
            regDoctor.setQualification(doctor.getQualification());
            regDoctor.setSpecialization(doctor.getSpecialization());
            regDoctor.setPassword(doctor.getPassword());
            regDoctor.setConfirmPassword(doctor.getConfirmPassword());
            regDoctor.setRoles(Collections.singleton(ROLES.DOCTOR));

            return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(regDoctor));
        }

    }
}
