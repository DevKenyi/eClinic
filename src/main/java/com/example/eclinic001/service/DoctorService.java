package com.example.eclinic001.service;

import com.example.eclinic001.configuration.sercuityConfiguration.jwt.authorizer.AccessTokenValidator;
import com.example.eclinic001.enums.ROLES;
import com.example.eclinic001.jwtConfiguration.JwtUtil;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.print.Doc;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DoctorService {
    @Autowired
    private DoctorsRepo repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private AccessTokenValidator accessTokenValidator;

    public ResponseEntity<Doctor> registerDoctor(Doctor doctor) {

        if (repo.findDoctorByEmail(doctor.getEmail()) != null) {
            throw new UsernameNotFoundException("This Doctor with " + doctor.getEmail() + " currently exist");

        } else {
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

    public ResponseEntity<List<Doctor>> doctorsList(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || authorizationHeader.isEmpty()) {
                log.info("Token is null, check token: " + authorizationHeader);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String token = authorizationHeader.substring(7);
            String extractUsername = JwtUtil.extractUsername(token);
            log.info("See extracted token here: " + extractUsername);

            Patient patient = patientRepo.findPatientByEmail(extractUsername);
            if (patient != null) {
                return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error retrieving patient appointments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }


    }

    public ResponseEntity<Doctor> findDoctorById(Long doctorsId, String authorizationHeader) {
        ResponseEntity<?> tokenValidator = accessTokenValidator.verifyDoctorTokenByEmail(authorizationHeader);
        Doctor doctorById = repo.findDoctorByDoctorId(doctorsId);
        try {
            if (tokenValidator != null && doctorById != null) {
                return new ResponseEntity<>(doctorById, HttpStatus.OK);
            }
        } catch (UsernameNotFoundException e) {
              log.error("Doctor with this id "+ doctorsId +"Was not found in our database");
        }

        return ResponseEntity.badRequest().build();

    }
}