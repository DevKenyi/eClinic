package com.example.eclinic001.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDtoTest {
    @Id
    Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String login;
    private String token;



}
