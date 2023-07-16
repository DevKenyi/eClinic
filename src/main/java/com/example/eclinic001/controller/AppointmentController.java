package com.example.eclinic001.controller;

import com.example.eclinic001.model.Appointments;
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
    @Autowired
    private AppointmentRepo aRepo;


//    @GetMapping("/appointments/{id}")
//    public ResponseEntity<List<Appointments>> getAppointmentList(@RequestHeader("Authorization") String token, String username, @PathVariable Long id) {
//        try {
//            Long userId = JwtUtil.extractUserIdFromJwtToken(token);
//            log.info("Checking the value of the token being passed: " + userId);
//
//           // return appointmentService.getAppointmentsByPatientId(username, token, id);
//        } catch (Exception e) {
//            log.error("Error retrieving appointment list: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

     @GetMapping("/appointments/{id}")
    public List< Appointments> getPatientAppointment(@PathVariable Long id){
        return appointmentService.patientAppointment(id);
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointments>> getAppointmentForPatients(@RequestHeader("Authorization") String authorization){
         return appointmentService.getPatientAppointment(authorization);
    }

}
