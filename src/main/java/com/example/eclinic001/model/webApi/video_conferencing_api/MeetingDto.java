package com.example.eclinic001.model.webApi.video_conferencing_api;

import com.example.eclinic001.model.Doctor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MeetingDto {
    @Id
    private UUID meetingId;
    private String title;
    private String createdAt;
    private String status;
    @ManyToOne
    private Doctor doctor;

    public MeetingDto(String message) {
    }
}
