package com.example.eclinic001.model;

import com.example.eclinic001.GENDER;
import com.example.eclinic001.enums.ROLES;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public abstract class User {
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "last_name")
    private String lastname;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GENDER gender;
    @Column(name = "birth")
    private String dob;
    @Column(name = "email")
    private String email;
    @Column(name = "Phone_number")
    private String phoneNumber;
    @Column(name = "password")
    @NotNull
    @NotBlank
    @NotEmpty
    private String password;
    @Transient
    @NotNull
    @NotBlank
    @NotEmpty
    private String confirmPassword;
    @Column(name = "user_roles")
    @Enumerated(EnumType.STRING)
    private Collection<ROLES> roles;


    public void validatePassword() {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("Password is empty");
        }
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }
}
