package com.example.eclinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@NoArgsConstructor
@Data
@MappedSuperclass
public abstract class Profile {
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String imageUrl;

    @Transient
    private MultipartFile imageFile;

    public void setImageFile(MultipartFile imageFile) {
        imageUrl = "/images/"+ imageFile.getOriginalFilename();
        this.imageFile = imageFile;
    }
}
