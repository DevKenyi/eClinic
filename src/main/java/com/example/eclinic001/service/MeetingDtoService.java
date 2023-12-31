package com.example.eclinic001.service;

import com.example.eclinic001.configuration.sercuityConfiguration.jwt.authorizer.AccessTokenValidator;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.webApi.video_conferencing_api.DyteMeetingRequest;
import com.example.eclinic001.model.webApi.video_conferencing_api.MeetingDto;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.MeetingDtoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class MeetingDtoService {
    @Autowired
    private MeetingDtoRepo meetingDtoRepo;
    @Autowired
    private AccessTokenValidator accessTokenValidator;
    @Autowired
    private DoctorsRepo doctorsRepo;

    public ResponseEntity<List<MeetingDto>> getMeeting(String authorizationHeader) {
        ResponseEntity<?> validateUser = accessTokenValidator.verifyDoctorTokenByEmail(authorizationHeader);

        if (validateUser.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonList(new MeetingDto("You are not authorized")));
        }

        List<MeetingDto> meetingDtoList = meetingDtoRepo.findAll();

        HttpStatus status = meetingDtoList.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(status).body(meetingDtoList);
    }

    public ResponseEntity<List<MeetingDto>> findMeetingByDoctorId(Long doctorId, String authHeader){
        ResponseEntity<?> validateUser = accessTokenValidator.verifyDoctorTokenByEmail(authHeader);

        if (validateUser.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonList(new MeetingDto("You are not authorized")));
        }
        Doctor doctorById = doctorsRepo.findDoctorByDoctorId(doctorId);
        if(doctorById== null){
            return  ResponseEntity.notFound().build();
        }
        else {
               List<MeetingDto> meetingDtoByDoctorDoctorId =
                                 meetingDtoRepo.
                                findMeetingDtoByDoctorDoctorId(doctorId);

               return ResponseEntity.ok().body(meetingDtoByDoctorDoctorId);
        }
    }

}




