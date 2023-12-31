package com.example.eclinic001.controller;

import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;
import java.util.List;

@RestController
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/doctors-list")
    public ResponseEntity<List<Doctor>> doctorList(@RequestHeader("Authorization") String token){
        return doctorService.doctorsList(token);
    }

    @GetMapping("/doctor-profile/{doctorId}")
    public ResponseEntity<Doctor> findDoctorById(@RequestHeader("Authorization") String token, @PathVariable Long doctorId){
        return doctorService.findDoctorById(doctorId, token);
    }
}
