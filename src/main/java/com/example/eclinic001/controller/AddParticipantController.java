package com.example.eclinic001.controller;

import com.example.eclinic001.model.webApi.video_conferencing_api.DyteParticipantRequest;
import com.example.eclinic001.service.DyteParticipantsRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class AddParticipantController {

    private final DyteParticipantsRequestService dyteParticipantsRequestService;

    @Autowired
    public AddParticipantController(DyteParticipantsRequestService dyteParticipantsRequestService) {
        this.dyteParticipantsRequestService = dyteParticipantsRequestService;
    }

    @PostMapping("/meetings/{meetingId}/participants/{doctorId}")
    public ResponseEntity<String> addParticipant(
            @PathVariable Long doctorId,
            @PathVariable String meetingId,

            @RequestBody DyteParticipantRequest dyteParticipantRequest,
            @RequestHeader("Authorization") String authorizationHeader)
             {

        // Ensure the preset_name is set
        dyteParticipantRequest.setPreset_name("webinar_presenter");

        return dyteParticipantsRequestService.addParticipant(doctorId, meetingId, dyteParticipantRequest, authorizationHeader );
    }

}