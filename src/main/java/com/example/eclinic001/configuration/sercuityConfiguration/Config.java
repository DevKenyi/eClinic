package com.example.eclinic001.configuration.sercuityConfiguration;

import com.example.eclinic001.enums.ROLES;
import com.example.eclinic001.jwtConfiguration.JwtTokenFilter;
import com.example.eclinic001.jwtConfiguration.JwtUtil;
import com.example.eclinic001.model.Admin;
import com.example.eclinic001.model.Doctor;
import com.example.eclinic001.model.Patient;
import com.example.eclinic001.repo.AdminRepo;
import com.example.eclinic001.repo.DoctorsRepo;
import com.example.eclinic001.repo.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
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


public class Config  {
    private  UrlBasedCorsConfigurationSource source;
    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain authorizationFilter(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.maximumSessions(1)
                        .expiredUrl("/login")
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeRequests(requests -> {
                            try {
                                requests
                                        .requestMatchers(("/admin/**")).hasRole("ADMIN")
                                        .requestMatchers("/patient","/login", "/doctor","/test").permitAll()
                                        .requestMatchers("/appointments/**","/").hasRole("PATIENT")
                                        .anyRequest().authenticated()
                                        .and()
                                        .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                                        .sessionManagement((sessionManagement)->
                                                sessionManagement.
                                                        sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                                        .maximumSessions(1)
                                                        .expiredUrl("/login?expired"))

                                ;
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform-login")
                        .defaultSuccessUrl("/dashboard")
                        .failureForwardUrl("/login?error=true")
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }


    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter(jwtUtil, customUserDetailsService );
    }


    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, @Qualifier("myPasswordEncoder") PasswordEncoder passwordEncoder){
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
    @Bean(name="myPasswordEncoder")
    public PasswordEncoder getPasswordEncoder() {
        DelegatingPasswordEncoder delPasswordEncoder=  (DelegatingPasswordEncoder)PasswordEncoderFactories.createDelegatingPasswordEncoder();
        BCryptPasswordEncoder bcryptPasswordEncoder =new BCryptPasswordEncoder();
        delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
        return delPasswordEncoder;
    }
    @Bean

    public HttpFirewall firewall(){
       return new DefaultHttpFirewall();
    }





}
