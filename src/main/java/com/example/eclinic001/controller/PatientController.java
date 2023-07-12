package com.example.eclinic001.controller;

import com.example.eclinic001.model.Patient;
import com.example.eclinic001.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
