package com.example.eclinic001.configuration.sercuityConfiguration;

import com.example.eclinic001.enums.ROLES;
import com.example.eclinic001.model.Admin;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.AdminRepo;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepo adminRepo;
    private final PatientRepo patientRepo;
    private final DoctorsRepo doctorsRepo;

    public CustomUserDetailsService(AdminRepo adminRepo, PatientRepo patientRepo, DoctorsRepo doctorsRepo) {
        this.adminRepo = adminRepo;
        this.patientRepo = patientRepo;
        this.doctorsRepo = doctorsRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminRepo.findAdminByEmail(username);

        if (admin != null) {
            return User.builder()
                    .username(admin.getEmail())
                    .password(admin.getPassword())
                    .authorities(mapAuthorities(admin.getRoles()))
                    .credentialsExpired(false)
                    .accountLocked(false)
                    .disabled(false)
                    .accountExpired(false)
                    .build();

        }
        Patient patient = patientRepo.findPatientByEmail(username);
        if (patient != null) {

            return User.builder()
                    .username(patient.getEmail())
                    .password(patient.getPassword())
                    .disabled(false)
                    .accountLocked(false)
                    .accountExpired(false)
                    .credentialsExpired(false)
                    .authorities(mapAuthorities(patient.getRoles()))
                    .build();
        }
        Doctor doctor = doctorsRepo.findDoctorByEmail(username);
        if (doctor != null) {
            return User.builder()
                    .username(doctor.getEmail())
                    .password(doctor.getPassword())
                    .disabled(false)
                    .accountLocked(false)
                    .accountExpired(false)
                    .credentialsExpired(false)
                    .authorities(mapAuthorities(doctor.getRoles()))
                    .build();
        }
        throw new UsernameNotFoundException("User not found "+ HttpStatus.NOT_FOUND);

    }

    private Collection<? extends GrantedAuthority> mapAuthorities(Collection<ROLES> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getAuthorities()))
                .collect(Collectors.toList());
    }
}