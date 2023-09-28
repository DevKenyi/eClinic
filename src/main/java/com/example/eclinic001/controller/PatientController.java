package com.example.eclinic001.controller;

import com.example.eclinic001.model.Patient;
import com.example.eclinic001.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class PatientController {
    @Autowired
    private PatientService service;
    @GetMapping("/patient")
    public ResponseEntity<List<Patient>> listOfPatient(){
        return service.patientList();
    }
    @GetMapping("/user")
    public ResponseEntity<Patient> authPatient(Authentication authentication){
        return service.userInfo(authentication);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Patient> findPatientById(@PathVariable Long patientId, @RequestHeader("Authorization") String authorizationHeader) {
        return service.findPatientById(patientId, authorizationHeader);
    }

    @GetMapping("patient/patient-blood-group/{patientId}")
    public ResponseEntity<String> patientBloodGroup(@PathVariable Long patientId, @RequestHeader("Authorization") String authorizationHeader){
        return service.patientBloodGroup(patientId, authorizationHeader);
    }

}
