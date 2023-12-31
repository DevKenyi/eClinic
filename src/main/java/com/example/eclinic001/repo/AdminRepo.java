package com.example.eclinic001.repo;

import com.example.eclinic001.model.Admin;
import com.example.eclinic001.model.Appointments;
import com.example.eclinic001.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {
    Admin findAdminByEmail(String email);




}
