package com.example.eclinic001.service;

import com.example.eclinic001.enums.ROLES;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class PatientService {

    private final PasswordEncoder passwordEncoder;
    private final PatientRepo repo;

    @Autowired
    public PatientService(PasswordEncoder passwordEncoder, PatientRepo repo) {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;
    }

    public ResponseEntity<Patient> registerPatients(Patient patient) {
        if (repo.findPatientByEmail(patient.getEmail()) != null) {
            throw new UsernameNotFoundException("This user already exists in our database");
        } else {
            patient.validatePassword();
            patient.encodePassword(passwordEncoder);

            Patient newPatient = new Patient();
            newPatient.setFirstname(patient.getFirstname());
            newPatient.setLastname(patient.getLastname());

            // Saving date of birth object as a string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            newPatient.setDob(formatter.format(formatter.parse(patient.getDob())));

            newPatient.setEmail(patient.getEmail());
            newPatient.setGender(patient.getGender());

            newPatient.setPassword(patient.getPassword());
            System.out.println("Check this encoded password " + patient.getPassword());

            newPatient.setPhoneNumber(patient.getPhoneNumber());
            newPatient.setRoles(Collections.singleton(ROLES.PATIENT));

            return new ResponseEntity<>(repo.save(newPatient), HttpStatus.OK);
        }
    }



//    private boolean passwordsMatch(String password, String confirmPassword) {
//        return password != null && !password.isEmpty() && password.equals(confirmPassword);
//    }

    public ResponseEntity<List<Patient>> patientList() {
        return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }
}