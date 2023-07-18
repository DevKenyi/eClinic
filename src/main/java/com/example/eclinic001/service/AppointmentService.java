package com.example.eclinic001.service;

import com.example.eclinic001.jwtConfiguration.JwtUtil;
import com.example.eclinic001.model.Appointments;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.AppointmentRepo;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AppointmentService {
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private DoctorsRepo doctorsRepo;
    @Autowired
    private AppointmentRepo appointmentRepo;
    @Autowired
    private UserDetailsService userDetailsService;

    public ResponseEntity<Appointments> bookAppointment(Appointments appointment, String patientEmail, Doctor doctor) {
        // Find the patient by email in the database
        Patient patient = patientRepo.findPatientByEmail(patientEmail);

        // If patient is found in the database and doctor availability is true, book the appointment
        if (patient != null && doctor.isAvailability()) {
            // Create a new Appointment
            Appointments bookAppointment = new Appointments();

            // Set the appointment properties
            bookAppointment.setAppointmentDateTime(appointment.getAppointmentDateTime());
            bookAppointment.setPurpose(appointment.getPurpose());

            bookAppointment.setDoctor(doctor);

            // Save the appointment
            Appointments savedAppointment = appointmentRepo.save(bookAppointment);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
        }

        // Return appropriate response if conditions are not met
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


    public List<Appointments> patientAppointment(Long id) {
        return appointmentRepo.findAppointmentByPatientId(id);
    }

    public ResponseEntity<List<Appointments>> getPatientAppointment(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || authorizationHeader.isEmpty()) {
                log.info("Token is null, check token: " + authorizationHeader);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix from the token
            String extractUsername = JwtUtil.extractUsername(token);
            log.info("See extracted token here: " + extractUsername);
            Patient patient = patientRepo.findPatientByEmail(extractUsername);
            if (patient != null) {
                List<Appointments> appointments = appointmentRepo.findAppointmentByPatientId(patient.getPatientId());
                return new ResponseEntity<>(appointments, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error retrieving patient appointments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}