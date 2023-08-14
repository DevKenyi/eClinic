package com.example.eclinic001.service;

import ch.qos.logback.core.read.ListAppender;
import com.example.eclinic001.enums.AppointmentStatus;
import com.example.eclinic001.jwtConfiguration.JwtUtil;
import com.example.eclinic001.jwtConfiguration.TokenAuthorization;
import com.example.eclinic001.model.Appointments;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.AppointmentRepo;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @Autowired
    private TokenAuthorization tokenAuthorization;

    public ResponseEntity<Appointments> bookAppointment(
            Long doctorId,
            String authorizationHeader,
            Appointments patientAppointment
    ) {
        try {
            Doctor doctor = doctorsRepo.findDoctorByDoctorId(doctorId);
            if (doctor == null) {
                return ResponseEntity.notFound().build();
            }

            String extractedEmail = tokenAuthorization.authorizeToken(authorizationHeader);
            Patient patient = patientRepo.findPatientByEmail(extractedEmail);
            if (patient != null && doctor.isAvailability()) {
                Appointments appointments = new Appointments();
                appointments.setAppointmentDateTime(patientAppointment.getAppointmentDateTime());
                appointments.setPatient(patient);
                appointments.setDoctor(doctor);
                appointments.setPurpose(patientAppointment.getPurpose());
                appointments.setAppointmentStatus(AppointmentStatus.Pending);
                Appointments bookedAppointment = appointmentRepo.save(appointments);
                return ResponseEntity.status(HttpStatus.CREATED).body(bookedAppointment);
            }
            else {
                  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Some error occurred: " + e.getMessage(), e);
        }

        return ResponseEntity.badRequest().build();
    }




    public List<Appointments> patientAppointment(Long id) {
        return appointmentRepo.findAppointmentByPatientId(id);
    }



    public ResponseEntity<List<Appointments>> getPatientAppointment2(@RequestHeader("Authorization") String authorizationHeader){
        TokenAuthorization tokenAuthorization = new TokenAuthorization();
        try{
          String extractedUsername =   tokenAuthorization.authorizeToken(authorizationHeader);
          Patient patient = patientRepo.findPatientByEmail(extractedUsername);
          if(patient!=null){
              List<Appointments> appointments = appointmentRepo.findAppointmentByPatientId(patient.getPatientId());
              return new ResponseEntity<>(appointments, HttpStatus.OK);

          }
          else {
              return ResponseEntity.notFound().build();
          }
        }
        catch (Exception e){
            log.error("Error retrieving patient appointments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}