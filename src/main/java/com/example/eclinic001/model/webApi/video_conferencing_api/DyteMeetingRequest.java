package com.example.eclinic001.model.webApi.video_conferencing_api;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DyteMeetingRequest {
    @JsonProperty("title")
    private String title;

    @JsonProperty("preferred_region")
    private String preferredRegion;

    @JsonProperty("record_on_start")
    private boolean recordOnStart;

    @JsonProperty("live_stream_on_start")
    private boolean liveStreamOnStart;

    @JsonProperty("recording_config")
    private RecordingConfig recordingConfig;

    @JsonProperty("dyte_bucket_config")
    private DyteBucketConfig dyteBucketConfig;

    @JsonProperty("live_streaming_config")
    private LiveStreamingConfig liveStreamingConfig;

    // Getters and setters here...

    // Nested classes to represent the nested structures in the JSON

    @Data
    private static class RecordingConfig {

        @JsonProperty("max_seconds")
        private int maxSeconds;

        @JsonProperty("file_name_prefix")
        private String fileNamePrefix;

        @JsonProperty("video_config")
        private VideoConfig videoConfig;

        @JsonProperty("audio_config")
        private AudioConfig audioConfig;

        @JsonProperty("storage_config")
        private StorageConfig storageConfig;

        // Getters and setters here...
    }

    @Data
    private static class VideoConfig {

        @JsonProperty("codec")
        private String codec;

        @JsonProperty("width")
        private int width;

        @JsonProperty("height")
        private int height;

        @JsonProperty("watermark")
        private Watermark watermark;

        // Getters and setters here...
    }
     @Data
    private static class Watermark {

        @JsonProperty("url")
        private String url;

        @JsonProperty("size")
        private Size size;

        @JsonProperty("position")
        private String position;

        // Getters and setters here...
    }
    @Data
    private static class Size {

        @JsonProperty("width")
        private int width;

        @JsonProperty("height")
        private int height;

        // Getters and setters here...
    }
    @Data
    private static class AudioConfig {

        @JsonProperty("codec")
        private String codec;

        @JsonProperty("channel")
        private String channel;

        // Getters and setters here...
    }
    @Data
    private static class StorageConfig {

        @JsonProperty("type")
        private String type;

        @JsonProperty("access_key")
        private String accessKey;

        @JsonProperty("secret")
        private String secret;

        @JsonProperty("bucket")
        private String bucket;

        @JsonProperty("region")
        private String region;

        @JsonProperty("path")
        private String path;

        @JsonProperty("auth_method")
        private String authMethod;

        @JsonProperty("username")
        private String username;

        @JsonProperty("password")
        private String password;

        @JsonProperty("host")
        private String host;

        @JsonProperty("port")
        private int port;

        @JsonProperty("private_key")
        private String privateKey;

        // Getters and setters here...
    }
    @Data
    private static class DyteBucketConfig {

        @JsonProperty("enabled")
        private boolean enabled;

        // Getters and setters here...
    }
    @Data
    private static class LiveStreamingConfig {

        @JsonProperty("rtmp_url")
        private String rtmpUrl;

        // Getters and setters here...
    }


}
