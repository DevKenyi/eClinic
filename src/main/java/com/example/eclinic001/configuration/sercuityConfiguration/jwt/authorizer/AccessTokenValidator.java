package com.example.eclinic001.configuration.sercuityConfiguration.jwt.authorizer;

import com.example.eclinic001.jwtConfiguration.TokenAuthorization;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccessTokenValidator {

    @Autowired
    private TokenAuthorization tokenAuthorization;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private DoctorsRepo doctorsRepo;

    public String getAuthorizationHeader(String authorizationHeader){
        return tokenAuthorization.authorizeToken(authorizationHeader);
    }

    public ResponseEntity<?> verifyDoctorTokenByEmail(String authHeader){
        String extractEmailFromToken = getAuthorizationHeader(authHeader);
       Doctor findDoctorByEmail =  doctorsRepo.findDoctorByEmail(extractEmailFromToken);
       if(findDoctorByEmail!=null){
           return ResponseEntity.status(HttpStatus.OK).build();
       }
       else
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found or token is invalid");

    }

    public ResponseEntity<?> verifyPatientTokenByEmail(String authHeader){
        String extractEmailFromToken = getAuthorizationHeader(authHeader);
        Patient findPatientByEmail =  patientRepo.findPatientByEmail(extractEmailFromToken);
        if(findPatientByEmail!=null){
            log.info("patient was found in repo see details "+ findPatientByEmail);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else
            log.info("patient was not found  from the supplied token::: "+ authHeader);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found or token is invalid");

    }







}
