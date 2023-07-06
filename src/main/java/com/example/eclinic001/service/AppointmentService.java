package com.example.eclinic001.service;

import com.example.eclinic001.model.Appointments;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.AppointmentRepo;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public ResponseEntity<Appointments> bookAppointment(Appointments appointment, Patient patient) {
        Optional<Patient> optionalPatient = patientRepo.findById(patient.getPatientId());
        if (optionalPatient.isPresent()) {
            Patient foundPatient = optionalPatient.get();
            foundPatient.getAppointments().add(appointment);

            patientRepo.save(foundPatient);
            return new ResponseEntity<>(appointment, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Appointments>> appointmentsResponseEntityList (){
        try {

              return ResponseEntity.status(HttpStatus.OK).body(appointmentRepo.findAll());
        }
        catch (Exception e){
            System.out.println("Some exception"+ Arrays.toString(e.getStackTrace()));
        }
        return  (ResponseEntity<List<Appointments>>) ResponseEntity.status(HttpStatus.NOT_FOUND);
    }



}
