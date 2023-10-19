package com.example.eclinic001.service;

import com.example.eclinic001.configuration.sercuityConfiguration.jwt.authorizer.AccessTokenValidator;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.webApi.video_conferencing_api.DyteApiResponse;
import com.example.eclinic001.model.webApi.video_conferencing_api.DyteApiResponseData;
import com.example.eclinic001.model.webApi.video_conferencing_api.DyteMeetingRequest;
import com.example.eclinic001.model.webApi.video_conferencing_api.MeetingDto;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.MeetingDtoRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Service
@Slf4j

public class DyteMeetingRequestService {
    @Value("${dyte.api.baseurl}")
    private String baseUrl;

    private RestTemplate restTemplate;
    @Autowired
    private DoctorsRepo doctorsRepo;
    @Autowired
    private AccessTokenValidator accessTokenValidator;
    @Autowired
    private MeetingDtoService meetingDtoService;
    private ResponseEntity<String> responseEntity;
    @Autowired
    private MeetingDtoRepo meetingDtoRepo;

    @Autowired
    public DyteMeetingRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> createMeeting(Long doctorId, DyteMeetingRequest dyteMeetingRequest, String authorization) {
        // Find the doctor by ID
        Doctor doctorById = doctorsRepo.findDoctorByDoctorId(doctorId);

        if (doctorById == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Verify the doctor's authorization
            accessTokenValidator.verifyDoctorTokenByEmail(authorization);

            // Build the API URL
            String createMeetingUrl = baseUrl + "/meetings";
            System.out.println("URL: " + createMeetingUrl);

            // Create HTTP headers with Basic Authentication
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("cdbb57e1-a2fd-4eb0-b051-dbf96ff6ee20", "27c8515bb47830275ecb");

            // Create the HTTP request entity
            HttpEntity<DyteMeetingRequest> requestHttpEntity = new HttpEntity<>(dyteMeetingRequest, httpHeaders);

            // API call and return response
             responseEntity = restTemplate.exchange(createMeetingUrl, HttpMethod.POST, requestHttpEntity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                log.info("Meeting was created successfully"+ requestHttpEntity.getBody());
                 saveMeetingFromDyteApiResponse(responseEntity);


                log.info("Request being sent: " + dyteMeetingRequest);
                log.info("Response received: " + responseEntity.getBody());
                return ResponseEntity.status(HttpStatus.CREATED).body("Meeting created successfully: " + responseEntity.getBody());
            } else {
                log.info("Error Creating Meeting"+ requestHttpEntity.getBody());
                return ResponseEntity.status(responseEntity.getStatusCode()).body("Failed to create meeting: " + responseEntity.getBody());

            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> testController (String authHeader){
        ResponseEntity<?> responseEntity = accessTokenValidator.verifyDoctorTokenByEmail(authHeader);
        log.info("see logs for dyte meeting request "+ responseEntity);
        return ResponseEntity.status(HttpStatus.OK).body("video api working good");

    }

    public void saveMeetingFromDyteApiResponse(ResponseEntity<String> responseEntity) {
        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                // Parse the response to a DyteApiResponse object
                DyteApiResponse dyteApiResponse = objectMapper.readValue(responseBody, DyteApiResponse.class);

                // Extract relevant data and save to your database
                DyteApiResponseData responseData = dyteApiResponse.getData();

                MeetingDto meetingDto = new MeetingDto();
                meetingDto.setMeetingId(responseData.getId());
                meetingDto.setTitle(responseData.getTitle());
                meetingDto.setCreatedAt(responseData.getCreatedAt());
                meetingDto.setStatus(responseData.getStatus());

                MeetingDto save = meetingDtoRepo.save(meetingDto);
                log.info("save object to repo "+ save);
            } catch (IOException e) {
                // Handle parsing exception
                e.printStackTrace();
            }
        }
    }
}
