package com.example.eclinic001.repo;


import com.example.eclinic001.model.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientProfileRepo extends JpaRepository<ProfilePicture, Long> {
}
