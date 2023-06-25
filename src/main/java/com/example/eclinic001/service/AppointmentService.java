package com.example.eclinic001.service;

import com.example.eclinic001.model.Appointments;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.AppointmentRepo;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
   private DoctorsRepo doctorsRepo;
    @Autowired

   private AppointmentRepo appointmentRepo;

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
}
