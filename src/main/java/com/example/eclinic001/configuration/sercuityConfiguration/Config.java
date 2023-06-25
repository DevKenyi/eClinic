package com.example.eclinic001.configuration.sercuityConfiguration;

import com.example.eclinic001.enums.ROLES;
import com.example.eclinic001.model.Admin;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.AdminRepo;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Slf4j

public class Config implements UserDetailsService   {

    private final AdminRepo adminRepo;

    private final PatientRepo patientRepo;

    private final DoctorsRepo doctorsRepo;

    private final PasswordEncoder passwordEncoder;

    private  UrlBasedCorsConfigurationSource source;

@Autowired
    public Config(AdminRepo adminRepo, PatientRepo patientRepo, DoctorsRepo doctorsRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.patientRepo = patientRepo;
        this.doctorsRepo = doctorsRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepo.findAdminByEmail(username);

        if (admin != null) {
            return User.builder()
                    .username(admin.getEmail())
                    .password(passwordEncoder.encode(admin.getPassword()))
                    .authorities(mapAuthorities(admin.getRoles()))
                    .credentialsExpired(false)
                    .accountLocked(false)
                    .disabled(false)
                    .accountExpired(false)
                    .build();
        }

        Patient patient = patientRepo.findPatientByEmail(username);
        if (patient != null) {
            String passwordChecks = passwordEncoder.encode(patient.getPassword());
            log.info("Stored password in Db: "+passwordChecks + " For user "+ patient.getEmail());

            return User.builder()
                    .username(patient.getEmail())
                    .password(passwordEncoder.encode(patient.getPassword()))
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
                    .password(passwordEncoder.encode(doctor.getPassword()))
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
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Bean
    public SecurityFilterChain authorizationFilter(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((request)-> {
                            try {
                                request
                                        .requestMatchers("/admin/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers("/anonymous*")
                                        .anonymous()
                                        .requestMatchers("/patient","/login")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                                        .and()
                                        .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                )
                .formLogin((form)->form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform-login")
                        .defaultSuccessUrl("/dashboard")
                        .failureForwardUrl("/failure")

                );
                return http.build();
    }



//    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider() {
//            @Override
//            protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
//                String rawPassword = authentication.getCredentials().toString();
//                String encodedPassword = userDetails.getPassword();
//                if (!customPasswordEncoder.passwordEncoder().matches(rawPassword, encodedPassword)) {
//                    throw new BadCredentialsException("Invalid password");
//                }
//            }
//        };
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(customPasswordEncoder.passwordEncoder());
//        return new ProviderManager(authProvider);
//    }



    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        System.out.println("Loading object "+ authProvider);

        authProvider.setUserDetailsService(userDetailsService);
        System.out.println("authProvider.setUserDetailsService(userDetailsService) " +"Loaded 1 " );

        authProvider.setPasswordEncoder(passwordEncoder);
        System.out.println("authProvider.setUserDetailsService(userDetailsService) " +"Loaded 2 " );
        return new ProviderManager(authProvider);
    }









    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;

    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
   @Bean
    public PasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
    }




}
