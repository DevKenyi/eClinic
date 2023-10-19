package com.example.eclinic001.controller;

import com.example.eclinic001.model.webApi.video_conferencing_api.MeetingDto;
import com.example.eclinic001.service.MeetingDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api")
@RestController
public class MeetingDtoController {
    @Autowired
    private MeetingDtoService service;

    /**
     * This API returns all the list of users in the meeting
     *
     *
     */
    @GetMapping("/meeting/dyte-response")
    public ResponseEntity<List<MeetingDto>> getMeeting(@RequestHeader("Authorization") String authorizationHeader) {
        return service.getMeeting(authorizationHeader);
    }
}
