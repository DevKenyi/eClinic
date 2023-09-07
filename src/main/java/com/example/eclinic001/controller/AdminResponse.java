package com.example.eclinic001.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminResponse {
    private Long adminId;
    private String jwtToken;
    private String userRole;
    private String userName;
    private String adminFirstname;
    private String adminLastname;
    private String email;
}
