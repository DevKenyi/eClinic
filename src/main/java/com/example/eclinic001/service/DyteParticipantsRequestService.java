package com.example.eclinic001.service;

import com.example.eclinic001.configuration.sercuityConfiguration.jwt.authorizer.AccessTokenValidator;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.model.webApi.video_conferencing_api.DyteParticipantRequest;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class DyteParticipantsRequestService {

    @Value("${dyte.api.baseurl}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final AccessTokenValidator accessTokenValidator;
    private final PatientRepo patientRepo;
    private final DoctorsRepo doctorsRepo;

    @Autowired

    public DyteParticipantsRequestService(RestTemplate restTemplate, AccessTokenValidator accessTokenValidator, PatientRepo patientRepo, DoctorsRepo doctorsRepo) {
        this.restTemplate = restTemplate;
        this.accessTokenValidator = accessTokenValidator;
        this.patientRepo = patientRepo;
        this.doctorsRepo = doctorsRepo;
    }

    public ResponseEntity<String> addParticipant(Long doctorId,
                                                 String meetingId,
                                                 DyteParticipantRequest addParticipant,
                                                 String authorizationHeader) {


        Doctor findDoctorById = doctorsRepo.findDoctorByDoctorId(doctorId);
        if (findDoctorById == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor with the id  "+ findDoctorById + "Was not found");
        }


        try {
            ResponseEntity<?> accessTokenEntity = accessTokenValidator.verifyDoctorTokenByEmail(authorizationHeader);
            if (accessTokenEntity.getStatusCode() != HttpStatus.OK) {
                // cases where access token validation fails
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access token validation failed");
            }

            String addParticipantUrl = baseUrl + "/meetings/" + meetingId + "/participants";
            log.info("add participant url here "+ addParticipantUrl);
            System.out.println("add participant url here "+ addParticipantUrl);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            log.info("httpheaders here "+ httpHeaders);
            httpHeaders.setBasicAuth("cdbb57e1-a2fd-4eb0-b051-dbf96ff6ee20", "27c8515bb47830275ecb");

            HttpEntity<DyteParticipantRequest> requestHttpEntity = new HttpEntity<>(addParticipant, httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(addParticipantUrl, HttpMethod.POST, requestHttpEntity, String.class);
            // Log the response details
            log.info("API Response - Status Code: {}", responseEntity.getStatusCode());
            log.info("API Response - Body: {}", responseEntity.getBody());
            System.out.println("Response Entity here "+ requestHttpEntity.getBody());


            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                log.info("Participant was added successfully: {}", responseEntity.getBody());
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("Participant was added successfully"+responseEntity.getBody());

            } else {
                return ResponseEntity.status(responseEntity.
                        getStatusCode()).body(responseEntity.getBody()+" Error adding participant");
            }
        } catch(Exception e) {
            log.error("Error adding participant: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }


}


