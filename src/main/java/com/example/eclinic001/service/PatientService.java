package com.example.eclinic001.service;

import com.example.eclinic001.configuration.sercuityConfiguration.jwt.authorizer.AccessTokenValidator;
import com.example.eclinic001.enums.ROLES;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class PatientService {

    private final PasswordEncoder passwordEncoder;
    private final PatientRepo repo;

    @Autowired
    private AccessTokenValidator accessTokenValidator;


    @Autowired
    public PatientService(PasswordEncoder passwordEncoder, PatientRepo repo) {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;
    }

    public ResponseEntity<Patient> registerPatients(Patient patient) throws Exception {
        if (repo.findPatientByEmail(patient.getEmail()) != null) {
            throw new UsernameNotFoundException("This user already exists in our database");
        } else {
            patient.validatePassword();
            patient.encodePassword(passwordEncoder);

            Patient newPatient = new Patient();
            newPatient.setFirstname(patient.getFirstname());
            newPatient.setLastname(patient.getLastname());

            // Saving date of birth object as a string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            newPatient.setDob(formatter.format(formatter.parse(patient.getDob())));

            log.info("see this format "+ patient.getDob());

            newPatient.setEmail(patient.getEmail());
            newPatient.setGender(patient.getGender());

            newPatient.setPassword(patient.getPassword());
            System.out.println("Check this encoded password " + patient.getPassword());

            newPatient.setPhoneNumber(patient.getPhoneNumber());
            newPatient.setRoles(Collections.singleton(ROLES.PATIENT));

            return new ResponseEntity<>(repo.save(newPatient), HttpStatus.CREATED);
        }


    }


    public ResponseEntity<List<Patient>> patientList() {
        return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }
    public ResponseEntity<Patient> userInfo(Authentication authentication){
        Patient patient = (Patient) authentication.getPrincipal();
        System.out.println(patient);
        return new ResponseEntity<>(patient, HttpStatus.FOUND);
    }

   public ResponseEntity<Patient> findPatientById(Long patientId, String authHeader){
       Patient findPatientById= repo.findPatientByPatientId(patientId);
        if(findPatientById!=null){
            try{
                accessTokenValidator.verifyPatientTokenByEmail(authHeader);
                log.info("Patient id here for debugging"+ patientId);
                return ResponseEntity.status(HttpStatus.OK).body(findPatientById);
            }
            catch (UsernameNotFoundException e){
                  log.error("User with "+ patientId+ "Was not found in database "+ e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
   }

   public ResponseEntity<String> patientBloodGroup(Long patientId, String authHeader){
        Patient patient = repo.findPatientByPatientId(patientId);
        if(patient!=null){
           try{
               accessTokenValidator.verifyPatientTokenByEmail(authHeader);
              String patientBloodGroup =  patient.getBloodGroup();
              if(patientBloodGroup.isEmpty()){
                  ResponseEntity.status(HttpStatus.CONFLICT).body("No blood group found for the user");
              }
              return  new ResponseEntity<>( patientBloodGroup, HttpStatus.OK);
           }
           catch (UsernameNotFoundException e){
               log.error("User with "+ patientId+ "Was not found in database "+ e.getMessage());
           }
        }


       return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
   }

}
