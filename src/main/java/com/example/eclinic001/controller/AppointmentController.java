package com.example.eclinic001.controller;

import com.example.eclinic001.model.Appointments;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.repo.AppointmentRepo;
import com.example.eclinic001.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = {"http://localhost:3000"}, allowedHeaders = "*", allowCredentials = "true")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

     @GetMapping("/appointments/{id}")
    public List< Appointments> getPatientAppointment(@PathVariable Long id){
        return appointmentService.patientAppointment(id);
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointments>> getAppointmentForPatients(@RequestHeader("Authorization") String authorization){
         return appointmentService.getPatientAppointment2(authorization);
    }

    @PostMapping("/appointment-bookings/{doctorId}")
    public ResponseEntity<Appointments> bookAppointment(
            @PathVariable Long doctorId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Appointments patientAppointment
    ) {
        return appointmentService.bookAppointment(doctorId, authorizationHeader, patientAppointment);
    }
    @GetMapping("/api/doctor/{doctorId}/pending-appointments")
    public ResponseEntity< List<Appointments>> getPendingAppointmentForDoctors(@PathVariable Long doctorId,
                                                                               @RequestHeader("Authorization") String authorization){
         return appointmentService.pendingAppointments(doctorId,authorization);
    }


}
