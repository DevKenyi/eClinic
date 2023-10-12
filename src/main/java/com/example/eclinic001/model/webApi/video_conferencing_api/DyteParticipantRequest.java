package com.example.eclinic001.model.webApi.video_conferencing_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DyteParticipantRequest {
    @JsonProperty("name")
    private String name;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("preset_name")
    private String preset_name;

    @JsonProperty("custom_participant_id")
    private String custom_participant_id;
}
