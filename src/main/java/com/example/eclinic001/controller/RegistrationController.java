package com.example.eclinic001.controller;

import com.example.eclinic001.GENDER;
import com.example.eclinic001.model.*;
import com.example.eclinic001.repo.AdminRepo;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import com.example.eclinic001.service.DoctorService;
import com.example.eclinic001.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@Slf4j
public class RegistrationController {
    @Autowired
    private PatientService service;
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
   private PasswordEncoder passwordEncoder;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private DoctorsRepo doctorsRepo;
    @Autowired
    private AdminRepo adminRepo;



    @Autowired
    public void setService(PatientService service) {
        this.service = service;
    }

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/patient")
    public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient) {
        return service.registerPatients(patient);
    }

    @PostMapping("/doctor")
    public ResponseEntity<Doctor> registerDoctor(@Valid @RequestBody Doctor doctor) {
        return doctorService.registerDoctor(doctor);
    }

    @GetMapping("/test")
    public String test() {
        return "API NOT SECURE";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest,
                                              HttpServletRequest request,
                                              HttpServletResponse response


    ) {
        System.out.println(loginRequest.toString());

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            boolean passwordChecking = passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword());
            System.out.println("<---------->" + passwordChecking + "<------------->");

            // Use the injected customPasswordEncoder bean instead of creating a new instance
            if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
                Authentication authentication = authenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                userDetails = (UserDetails) authentication.getPrincipal();
                log.info("Authentication Object here   " + authentication + "From " + RegistrationController.class);
                log.info("USER DETAILS here  " + userDetails + "From " + RegistrationController.class);

                String sessionToken = UUID.randomUUID().toString();

                // Fetch additional user details based on your requirements
                // For example, you can retrieve the user's role, name, email, etc.
                String userRole = userDetails.getAuthorities().iterator().next().getAuthority();
                String userName = userDetails.getUsername();

                String patientFirstname = null;
                String patientLastname = null;
                String patientEmail = null;
                String patientGenotype = null;
                String patientAddress = null;
                String dob = null;
                String phoneNumber = null;
                GENDER gender = null;
                String bloodGroup = null;


                String doctorFirstname = null;
                String doctorLastname = null;
                String specialization = null;
                boolean availability = false;
                Set<Patient> patientOnAppointment = null;


                String adminFirstname = null;
                String adminLastname = null;
                String adminEmail = null;


                if (userRole.equals("ROLE_PATIENT")) {
                    Patient patient = patientRepo.findPatientByEmail(loginRequest.getEmail());
                    if (patient != null) {
                        Long id = patient.getPatientId();
                        patientFirstname = patient.getFirstname();
                        patientLastname = patient.getLastname();
                        patientEmail  = patient.getEmail();
                        patientGenotype = patient.getGenotype();
                        patientAddress = patient.getAddress();
                        dob = patient.getDob();
                        phoneNumber = patient.getPhoneNumber();
                        gender = patient.getGender();
                        bloodGroup = patient.getBloodGroup();

                    }

                }

                if(userRole.equals("ROLE_DOCTOR")){
                    Doctor doctor = doctorsRepo.getDoctorByEmail(loginRequest.getEmail());
                    if(doctor!=null){
                        doctorFirstname = doctor.getFirstname();
                        doctorLastname = doctor.getLastname();
                        specialization = String.valueOf(doctor.getSpecialization());
                        availability = doctor.isAvailability();


                    }
                }

                if(userRole.equals("ROLE_ADMIN")){
                    Admin admin = adminRepo.findAdminByEmail(loginRequest.getEmail());
                    if(admin!=null){
                        adminFirstname = admin.getFirstname();
                        adminLastname = admin.getLastname();
                        adminEmail = admin.getEmail();

                    }
                }


                // Create a map to include the session token and user details in the response body
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("sessionToken", sessionToken);
                responseBody.put("userRole", userRole);
                responseBody.put("userName", userName);


                responseBody.put("patientFirstname", patientFirstname);
                responseBody.put("patientLastname", patientLastname);
                responseBody.put("patientEmail", patientEmail);
                responseBody.put("patientGenotype", patientGenotype);
                responseBody.put("patientAddress", patientAddress);
                responseBody.put("patientPhoneNumber", phoneNumber);
                responseBody.put("gender", gender);
                responseBody.put("bloodGroup", bloodGroup);



                responseBody.put("doctorFirstname", doctorFirstname);
                responseBody.put("doctorLastname", doctorLastname);
                responseBody.put("specialization", specialization);
                responseBody.put("availability", availability);

                responseBody.put("adminFistname", adminFirstname);
                responseBody.put("adminLastname", adminLastname);
                responseBody.put("email", adminEmail);
                responseBody.put("dob", dob);




                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION)
                        .body(responseBody);
            }

        } catch (AuthenticationException e) {

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }







}