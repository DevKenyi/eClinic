package com.example.eclinic001.jwtConfiguration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

@Slf4j
@Component
public class TokenAuthorization {
    public String authorizeToken(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            String tokenSubString = authorizationHeader.substring(7);
            // Extract token from header and extract username from the token
            return JwtUtil.extractUsername(tokenSubString);
        } else {
            throw new IllegalArgumentException("Authorization header is null or empty");
        }
    }
}