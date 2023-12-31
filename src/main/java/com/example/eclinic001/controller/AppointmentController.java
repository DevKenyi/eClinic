package com.example.eclinic001.controller;

import com.example.eclinic001.model.Appointments;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.AppointmentRepo;
import com.example.eclinic001.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
//@CrossOrigin(origins = {"http://localhost:3000"}, allowedHeaders = "*", allowCredentials = "true")
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


    @PutMapping("/api/confirm-appointment/{doctorId}/{patientId}/{appointmentId}")
    public ResponseEntity<?> confirmAppointment(@PathVariable Long doctorId, @PathVariable Long patientId, @RequestHeader("Authorization") String authorizationHeader,  @PathVariable Long appointmentId){
        return appointmentService.confirmAppointment(doctorId, patientId, authorizationHeader,  appointmentId);
    }

    @GetMapping("/api/doctor/{doctorId}/patients")
    public ResponseEntity<List<Patient>> findPatientByDoctorId(@PathVariable Long doctorId, @RequestHeader("Authorization")  String authorizationHeader){
        return appointmentService.findPatientByDoctorsId(doctorId, authorizationHeader);
    }

    @GetMapping("/api/doctor/{doctorId}/upcoming-appointments")
    public ResponseEntity< List<Appointments>> upcomingAppointments(@PathVariable Long doctorId,
                                                                    @RequestHeader("Authorization") String authorization){
        return appointmentService.upcomingAppointments(doctorId,authorization);
    }

    @GetMapping("/api/doctor/{doctorId}/in-progress")
    public ResponseEntity< List<Appointments>> inProgress(@PathVariable Long doctorId,
                                                                    @RequestHeader("Authorization") String authorization){
        return appointmentService.inProgress(doctorId,authorization);
    }

    @GetMapping("api/patient/{patientId}/patient/status-pending")
    public ResponseEntity<List<Integer>> pendingAppointmentForPatient(@PathVariable Long patientId, @RequestHeader ("Authorization") String authorizationHeader){
        return appointmentService.pendingAppointmentForPatient(patientId, authorizationHeader);
    }

    @GetMapping("api/patient/{patientId}/patient/status-scheduled")
    public ResponseEntity<List<Integer>> scheduledAppointmentForPatient(@PathVariable Long patientId, @RequestHeader ("Authorization") String authorizationHeader){
        return appointmentService.scheduledAppointmentForPatient(patientId, authorizationHeader);
    }

    @GetMapping("api/patient/{patientId}/patient/status-completed")
    public ResponseEntity<List<Integer>> completedAppointmentForPatient(@PathVariable Long patientId, @RequestHeader ("Authorization") String authorizationHeader){
        return appointmentService.completedAppointmentForPatient(patientId, authorizationHeader);
    }

    @GetMapping("api/patient/{patientId}/doctor-patient")
    public ResponseEntity<List<Doctor>> doctorForPatientBasedOnPatientId(@PathVariable Long patientId, @RequestHeader ("Authorization") String authorizationHeader){
        return appointmentService.doctorListForPatient(patientId, authorizationHeader);
    }

    @GetMapping("/api/v2/doctors/{doctorId}/appointments")
    public ResponseEntity<List<Appointments>> findAppointmentsForPatientsUsingDoctorsId(@PathVariable Long doctorId, @RequestHeader("Authorization") String authorizationHeader){
        return appointmentService.findAppointmentsForPatientsUsingDoctorsId(doctorId, authorizationHeader );
    }
   @PutMapping("/api/v1/updateStatus/{appointmentId}")
    public String updateAppointmentStatus(
                                            @RequestParam Long doctorId,
                                            @RequestParam  Long patientId,
                                            @PathVariable Long appointmentId,
                                            @RequestHeader ("Authorization") String authorizationHeader,
                                            @RequestBody   Appointments appointmentStatus



    )
    {
        return appointmentService.updateAppointmentStatus(doctorId, patientId, appointmentId, authorizationHeader, appointmentStatus);
    }
      @PutMapping("api/test/statusUpdate/{doctorId}/{patientId}/{appointmentId}")
    public String updateAppointmentTest(@PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long appointmentId, @RequestHeader("Authorization") String authorizationHeader, @RequestBody Appointments appointmentStatus){
        return appointmentService.updateAppointmentStatus(doctorId, patientId,appointmentId,authorizationHeader, appointmentStatus );
    }




}
