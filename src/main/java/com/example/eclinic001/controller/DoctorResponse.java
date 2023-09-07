package com.example.eclinic001.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    private String jwtToken;
    private Long id;
    private String userRole;
    private String userName;
    private String doctorFirstname;
    private String doctorLastname;
    private String specialization;
    private boolean availability;
}
