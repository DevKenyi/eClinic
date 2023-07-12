package com.example.eclinic001.controller;

import com.example.eclinic001.model.Appointments;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.AppointmentRepo;
import com.example.eclinic001.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"}, allowedHeaders = "*", allowCredentials = "true")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentRepo aRepo;
    @PostMapping("appointment/{id}")
    public ResponseEntity<Appointments> bookAppointment(@RequestBody Appointments appointments){
        return null;
    }

    @GetMapping("/appointments")

    public ResponseEntity<List<Appointments>> listResponseEntity() {
        return appointmentService.appointmentsResponseEntityList();
    }


}
