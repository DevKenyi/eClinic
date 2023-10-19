package com.example.eclinic001.model.webApi.video_conferencing_api;

import lombok.Data;

import java.util.UUID;

@Data
public class DyteApiResponseData {
    private UUID id;
    private String title;
    private String preferredRegion;
    private String createdAt;
    private boolean recordOnStart;
    private String Status;
}
