package com.example.eclinic001.controller;

import com.example.eclinic001.model.Appointments;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    @PostMapping("appointment/{id}")
    public ResponseEntity<Appointments> bookAppointment(@RequestBody Appointments appointments,
                                                        Patient patient,@PathVariable Long id){
        Patient findPatient= new Patient();
        findPatient.setPatientId(id);
        return appointmentService.bookAppointment(appointments, patient);
    }
}
