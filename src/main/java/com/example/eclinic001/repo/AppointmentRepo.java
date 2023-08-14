package com.example.eclinic001.repo;

import com.example.eclinic001.model.Appointments;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointments, Long> {
//    @Query("SELECT a FROM Appointments a JOIN a.doctor d JOIN d.patientsSet p WHERE p.patientId = :patientId")
//    List<Appointments> findAppointmentByPatientId(Long patientId);

    @Query("SELECT a FROM Appointments a JOIN a.patient p WHERE p.patientId = :patientId")
    List<Appointments> findAppointmentByPatientId(@Param("patientId") Long patientId);


}

