package com.example.eclinic001.controller;

import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.LoginRequest;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.service.DoctorService;
import com.example.eclinic001.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.Arrays;


@RestController
@Slf4j
public class RegistrationController {
    @Autowired
    private PatientService service;
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
   private PasswordEncoder passwordEncoder;


    @Autowired
    public void setService(PatientService service) {
        this.service = service;
    }

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/patient")
    public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient) {
        return service.registerPatients(patient);
    }

    @PostMapping("/doctor")
    public ResponseEntity<Doctor> registerDoctor(@Valid @RequestBody Doctor doctor) {
        return doctorService.registerDoctor(doctor);
    }

    @GetMapping("/test")
    public String test() {
        return "API NOT SECURE";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.toString());

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
           boolean passwordChecking =  passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword());
            System.out.println("<---------->"+ passwordChecking+"<------------->");

            System.out.println(loginRequest.getPassword());
            System.out.println();
            System.out.println(loginRequest.getEmail());
            System.out.println();
            System.out.println(userDetails.getUsername());
            System.out.println();
            System.out.println(userDetails.getPassword());

            for(int i=0;i<10; i++){
                System.out.println(userDetails.getPassword());
            }


            // Use the injected customPasswordEncoder bean instead of creating a new instance
            if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                log.info("-------------------------------this is being called here ---------------------------------");
                log.info("userDetails object coming from Registration Class: " + userDetails);

                log.info("AuthenticatedUser method loaded");
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("SecurityContextHolder.getContext called");

                return ResponseEntity.ok().build();
            }
            else ResponseEntity.status(HttpStatus.UNAUTHORIZED);


        } catch (AuthenticationException e) {
            log.error("An error has occurred and the catch is loaded" + Arrays.toString(e.getStackTrace()));
        }


        return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }





}