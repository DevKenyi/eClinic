package com.example.eclinic001.controller;

import com.example.eclinic001.GENDER;
import com.example.eclinic001.model.Profile;
import com.example.eclinic001.model.ProfilePicture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private String jwtToken;
    private String userRole;
    private String userName;
    private String patientFirstname;
    private String patientLastname;
    private String patientEmail;
    private String patientGenotype;
    private String patientAddress;
    private String dob;
    private String phoneNumber;
    private GENDER gender;
    private String bloodGroup;
   private ProfilePicture profilePicture;
}
