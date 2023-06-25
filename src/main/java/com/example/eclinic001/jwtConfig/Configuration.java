//package com.example.eclinic001.jwtConfig;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.HttpBasicDsl;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.stereotype.Component;
//
//@EnableWebSecurity
//@org.springframework.context.annotation.Configuration
//@RequiredArgsConstructor
//public class Configuration {
//    private  UserAuthenticationProvider userAuthenticationProvider;
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(httpSecuritySessionManagementConfigurer ->
//                        httpSecuritySessionManagementConfigurer.
//                                sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests((request)->
//                        request.requestMatchers(HttpMethod.POST, "/login","/register").permitAll()
//                                .anyRequest()
//                                .authenticated());
//        return http.build();
//
//    }
//}
