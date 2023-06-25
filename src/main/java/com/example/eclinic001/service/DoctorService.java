package com.example.eclinic001.service;

import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.repo.DoctorsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    @Autowired
    private DoctorsRepo repo;
    public ResponseEntity<Doctor> registerDoctor(Doctor doctor) {
       return new ResponseEntity<>( repo.save(doctor), HttpStatus.CREATED) ;
    }
}
