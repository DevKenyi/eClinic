package com.example.eclinic001.controller;

import com.example.eclinic001.jwtConfiguration.JwtUtil;
import com.example.eclinic001.model.Admin;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.LoginRequest;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.AdminRepo;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import com.example.eclinic001.service.DoctorService;
import com.example.eclinic001.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


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
    public ResponseEntity<Patient> registerPatient  (@RequestBody Patient patient) throws Exception {
        return service.registerPatients(patient);
    }

    @PostMapping("/doctor")
    public ResponseEntity<Doctor> registerDoctor(@RequestBody Doctor doctor) {
        return doctorService.registerDoctor(doctor);
    }

    @GetMapping("/test")
    public String test() {
        return "API FOR EVERYONE";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest,
                                              HttpServletRequest request,
                                              HttpServletResponse response


    ) {
        System.out.println(loginRequest.toString());

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
                Authentication authentication = authenticationManager.authenticate(token);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                context.getAuthentication();
                userDetails = (UserDetails) authentication.getPrincipal();

                String jwtToken = JwtUtil.generateToken(userDetails);
                response.setHeader("Authorization", "Bearer " + token);


                String userRole = userDetails.getAuthorities().iterator().next().getAuthority();
                String userName = userDetails.getUsername();

                if (userRole.equals("ROLE_PATIENT")) {
                    Patient patient = patientRepo.findPatientByEmail(loginRequest.getEmail());
                    if (patient != null) {
                        // Create and populate patient response object
                        PatientResponse patientResponse = new PatientResponse();
                        patientResponse.setJwtToken(jwtToken);
                        patientResponse.setUserRole(userRole);
                        patientResponse.setUserName(userName);
                        patientResponse.setPatientFirstname(patient.getFirstname());
                        patientResponse.setPatientLastname(patient.getLastname());
                        patientResponse.setPatientEmail(patient.getEmail());
                        patientResponse.setPatientGenotype(patient.getGenotype());
                        patientResponse.setPatientAddress(patient.getAddress());
                        patientResponse.setDob(patient.getDob());
                        patientResponse.setPhoneNumber(patient.getPhoneNumber());
                        patientResponse.setGender(patient.getGender());
                        patientResponse.setBloodGroup(patient.getBloodGroup());
                        patientResponse.setProfilePicture(patient.getProfilePicture());

                        return ResponseEntity.ok().body(patientResponse);
                    }
                } else if (userRole.equals("ROLE_DOCTOR")) {
                    Doctor doctor = doctorsRepo.getDoctorByEmail(loginRequest.getEmail());
                    if (doctor != null) {
                        // Create and populate doctor response object
                        DoctorResponse doctorResponse = new DoctorResponse();
                        doctorResponse.setJwtToken(jwtToken);
                        doctorResponse.setUserRole(userRole);
                        doctorResponse.setUserName(userName);
                        doctorResponse.setDoctorFirstname(doctor.getFirstname());
                        doctorResponse.setDoctorLastname(doctor.getLastname());
                        doctorResponse.setSpecialization(String.valueOf(doctor.getSpecialization()));
                        doctorResponse.setAvailability(doctor.isAvailability());

                        return ResponseEntity.ok().body(doctorResponse);
                    }
                } else if (userRole.equals("ROLE_ADMIN")) {
                    Admin admin = adminRepo.findAdminByEmail(loginRequest.getEmail());
                    if (admin != null) {
                        // Create and populate admin response object
                        AdminResponse adminResponse = new AdminResponse();
                        adminResponse.setJwtToken(jwtToken);
                        adminResponse.setUserRole(userRole);
                        adminResponse.setUserName(userName);
                        adminResponse.setAdminFirstname(admin.getFirstname());
                        adminResponse.setAdminLastname(admin.getLastname());
                        adminResponse.setEmail(admin.getEmail());

                        return ResponseEntity.ok().body(adminResponse);
                    }
                }
            }
        } catch (AuthenticationException e) {
            // Exception handling code...
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


    }


}