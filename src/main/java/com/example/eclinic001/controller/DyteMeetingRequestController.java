package com.example.eclinic001.controller;

import com.example.eclinic001.model.webApi.video_conferencing_api.DyteMeetingRequest;
import com.example.eclinic001.service.DyteMeetingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/video")

public class DyteMeetingRequestController {
    @Autowired
    private DyteMeetingRequestService dyteMeetingRequestService;



    @PostMapping("/meeting/{doctorId}")
    public ResponseEntity<String> createMeeting(
            @PathVariable Long doctorId,
            @RequestBody DyteMeetingRequest dyteMeetingRequest,
            @RequestHeader("Authorization") String authorization) {


        return dyteMeetingRequestService.createMeeting(doctorId, dyteMeetingRequest, authorization);
    }

    @GetMapping("/meeting-test")
    public ResponseEntity<String> testingDyteMeetingRequest(@RequestHeader("Authorization") String authorizationHeader){
        return dyteMeetingRequestService.testController(authorizationHeader);
    }

}
