package com.example.eclinic001.service;

import com.example.eclinic001.configuration.sercuityConfiguration.jwt.authorizer.AccessTokenValidator;
import com.example.eclinic001.enums.AppointmentStatus;
import com.example.eclinic001.jwtConfiguration.TokenAuthorization;
import com.example.eclinic001.model.Appointments;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.AppointmentRepo;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.print.Doc;
import java.util.*;

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
    private TokenAuthorization tokenAuthorization;
    private Doctor doctor;

    @Autowired
    private AccessTokenValidator accessTokenValidator;


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

                //Send a notification to doctor to confirm appointment
                return ResponseEntity.status(HttpStatus.CREATED).body(bookedAppointment);
            } else {
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


    public ResponseEntity<List<Appointments>> getPatientAppointment2(@RequestHeader("Authorization") String authorizationHeader) {
        TokenAuthorization tokenAuthorization = new TokenAuthorization();
        try {
            String extractedUsername = tokenAuthorization.authorizeToken(authorizationHeader);
            Patient patient = patientRepo.findPatientByEmail(extractedUsername);
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


    public ResponseEntity<List<Appointments>> pendingAppointments(Long doctorId, @RequestHeader("Authorization") String authorizationHeader) {

        Doctor doctor = doctorsRepo.findDoctorByDoctorId(doctorId);

        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        try {
            String extractUsername = tokenAuthorization.authorizeToken(authorizationHeader);
            Doctor findExtractedEmailDoctor = doctorsRepo.findDoctorByEmail(extractUsername);
            if (findExtractedEmailDoctor != null) {
                List<Appointments> appointmentsByAppointmentStatusAndDoctor = appointmentRepo.findAppointmentsByAppointmentStatusAndDoctor(AppointmentStatus.Pending, doctor);
                return new ResponseEntity<>(appointmentsByAppointmentStatusAndDoctor, HttpStatus.OK);
            }
        } catch (UsernameNotFoundException e) {
            log.error("Can not find doctor with this token, it is " +
                    "either the token is expired or the doctor doctor is not in our database ");
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Appointments>> upcomingAppointments(Long doctorId, String authHeader) {
        Doctor doctor = doctorsRepo.findDoctorByDoctorId(doctorId);

        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        try {
            accessTokenValidator.verifyDoctorTokenByEmail(authHeader);
            List<Appointments> appointmentsByAppointmentStatusAndDoctor = appointmentRepo.findAppointmentsByAppointmentStatusAndDoctor(AppointmentStatus.Scheduled, doctor);
            return new ResponseEntity<>(appointmentsByAppointmentStatusAndDoctor, HttpStatus.OK);

        } catch (UsernameNotFoundException e) {
            log.error("Can not find doctor with this token, it is " +
                    "either the token is expired or the doctor doctor is not in our database ");
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<List<Appointments>> inProgress(Long doctorId, String authHeader) {
        Doctor doctor = doctorsRepo.findDoctorByDoctorId(doctorId);

        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        try {
            accessTokenValidator.verifyDoctorTokenByEmail(authHeader);
            List<Appointments> appointmentsByAppointmentStatusAndDoctor = appointmentRepo.findAppointmentsByAppointmentStatusAndDoctor(AppointmentStatus.InProcess, doctor);
            return new ResponseEntity<>(appointmentsByAppointmentStatusAndDoctor, HttpStatus.OK);

        } catch (UsernameNotFoundException e) {
            log.error("Can not find doctor with this token, it is " +
                    "either the token is expired or the doctor doctor is not in our database ");
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<?> confirmAppointment(
            Long doctorId,
            Long patientId,
            String authorizationHeader,
            Long appointmentId) {
        try {
            String extractUsername = tokenAuthorization.authorizeToken(authorizationHeader);
            Doctor extractDocEmail = doctorsRepo.findDoctorByEmail(extractUsername);

            if (extractDocEmail == null || !Objects.equals(extractDocEmail.getDoctorId(), doctorId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
            }

            Optional<Appointments> findAppointmentById = appointmentRepo.findById(appointmentId);

            if (findAppointmentById.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
            }

            Appointments appointment = findAppointmentById.get();

            Optional<Patient> findPatientById = patientRepo.findById(patientId);
            if (findPatientById.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with the given id " + patientId + " was not found!");
            }

            if (appointment.getAppointmentStatus() == AppointmentStatus.Pending) {

                appointment.setAppointmentStatus(AppointmentStatus.Scheduled);
                Appointments updatedAppointment = appointmentRepo.save(appointment);

                // Implement notification and video link generation here
                // Send video link to the patient

                return ResponseEntity.ok(updatedAppointment);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment is not in Pending status");
            }
        } catch (Exception e) {
            log.error("An error occurred: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    public ResponseEntity<List<Patient>> findPatientByDoctorsId(Long doctorId, String authorizationHeader) {
        try {
            // Verify the token, this may throw a UsernameNotFoundException
            accessTokenValidator.verifyDoctorTokenByEmail(authorizationHeader);

            // Fetch patients by doctorId
            List<Patient> findPatientByDocId = appointmentRepo.findPatientsByDoctorId(doctorId);

            if (!findPatientByDocId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(findPatientByDocId);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
        } catch (UsernameNotFoundException e) {
            log.error("Can not find doctor with this token, it is " +
                    "either the token is expired or the doctor doctor is not in our database ");
        }
        return (ResponseEntity<List<Patient>>) ResponseEntity.status(HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<List<Integer>> pendingAppointmentForPatient(Long patientId, String authHeader) {
        Patient patient = patientRepo.findPatientByPatientId(patientId);

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        try {
            accessTokenValidator.verifyPatientTokenByEmail(authHeader);
            List<Appointments> appointmentsByAppointmentStatusAndPatient = appointmentRepo.findAppointmentsByAppointmentStatusAndPatient(AppointmentStatus.Pending, patient);
            Integer pendingAppointmentForPatient = appointmentsByAppointmentStatusAndPatient.size();

            // Wrap the pendingAppointmentForPatient in a list and return it
            List<Integer> result = Collections.singletonList(pendingAppointmentForPatient);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            // Handle exceptions here and return an appropriate ResponseEntity in case of errors.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    public ResponseEntity<List<Integer>> scheduledAppointmentForPatient(Long patientId, String authHeader) {
        Patient patient = patientRepo.findPatientByPatientId(patientId);

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        try {
            accessTokenValidator.verifyPatientTokenByEmail(authHeader);
            List<Appointments> appointmentsByAppointmentStatusAndPatient = appointmentRepo.findAppointmentsByAppointmentStatusAndPatient(AppointmentStatus.Scheduled, patient);
            Integer pendingAppointmentForPatient = appointmentsByAppointmentStatusAndPatient.size();

            // Wrap the pendingAppointmentForPatient in a list and return it
            List<Integer> result = Collections.singletonList(pendingAppointmentForPatient);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            // Handle exceptions here and return an appropriate ResponseEntity in case of errors.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }


    }

    public ResponseEntity<List<Integer>> completedAppointmentForPatient(Long patientId, String authHeader) {
        Patient patient = patientRepo.findPatientByPatientId(patientId);

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        try {
            accessTokenValidator.verifyPatientTokenByEmail(authHeader);
            List<Appointments> appointmentsByAppointmentStatusAndPatient = appointmentRepo.findAppointmentsByAppointmentStatusAndPatient(AppointmentStatus.Completed, patient);
            Integer pendingAppointmentForPatient = appointmentsByAppointmentStatusAndPatient.size();

            // Wrap the pendingAppointmentForPatient in a list and return it
            List<Integer> result = Collections.singletonList(pendingAppointmentForPatient);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            // Handle exceptions here and return an appropriate ResponseEntity in case of errors.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }


    }

    public ResponseEntity<List<Doctor>> doctorListForPatient(Long patientId, String authorizationHeader) {
        Patient patient = patientRepo.findPatientByPatientId(patientId);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            accessTokenValidator.verifyPatientTokenByEmail(authorizationHeader);
            List<Doctor> doctorsByPatientId = appointmentRepo.findDoctorByPatientId(patientId);

            // Check if the list is not empty
            if (!doctorsByPatientId.isEmpty()) {
                Doctor firstDoctorInArray = doctorsByPatientId.get(0);
                log.info("First doctor: " + firstDoctorInArray);

                // Create a list containing the first doctor
                List<Doctor> responseList = Collections.singletonList(firstDoctorInArray);

                // Return the response entity with the list
                return new ResponseEntity<>(responseList, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public ResponseEntity<List<Appointments>> findAppointmentsForPatientsUsingDoctorsId(Long doctorId, String authorizationHeader) {
        Doctor doctor = doctorsRepo.findDoctorByDoctorId(doctorId);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            accessTokenValidator.verifyDoctorTokenByEmail(authorizationHeader);
            List<Appointments> appointmentsList = appointmentRepo.findAppointmentsByDoctorDoctorId(doctorId);
            if (!appointmentsList.isEmpty()) {
                return new ResponseEntity<>(appointmentsList, HttpStatus.OK);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}