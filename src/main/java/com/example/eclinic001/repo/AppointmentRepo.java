package com.example.eclinic001.repo;

import com.example.eclinic001.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepo extends JpaRepository<Appointments, Long> {
}
