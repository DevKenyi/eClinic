package com.example.eclinic.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageController {
    private static final String IMAGE_DIRECTORY = "/src/main/resources/static/images/";
    private static final String IMAGE_DIRECTORY_DOCTOR = "/src/main/resources/static/images/doctors";


    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> savePatientImage(@PathVariable String filename) throws URISyntaxException, MalformedURLException {
        return getResourceResponseEntity(filename);

    }
    @GetMapping("/images/doctors/{filename:.+}")
    private ResponseEntity<Resource> getResourceResponseEntity_Doc(@PathVariable String filename) {
        return getResourceResponseEntity(filename, IMAGE_DIRECTORY_DOCTOR);
    }

    private ResponseEntity<Resource> getResourceResponseEntity(@PathVariable String filename, String imageDirectoryDoctor) {
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        Path imagePath = Paths.get(imageDirectoryDoctor + encodedFilename);
        Resource resourceUrl = UrlResource.from(imagePath.toUri());
        if (resourceUrl.exists() && resourceUrl.isReadable()) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resourceUrl);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<Resource> getResourceResponseEntity(@PathVariable String filename) {
        return getResourceResponseEntity(filename, IMAGE_DIRECTORY);
    }
}
