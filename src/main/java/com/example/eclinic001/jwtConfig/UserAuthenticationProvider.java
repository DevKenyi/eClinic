//package com.example.eclinic001.jwtConfig;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.example.eclinic001.model.UserDtoTest;
//import com.example.eclinic001.service.UserDtoTestService;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import java.util.Base64;
//import java.util.Collections;
//import java.util.Date;
//
//@RequiredArgsConstructor
//@Component
//public class UserAuthenticationProvider {
//    @Value("${security.jwt.toke.secret-key:secret-value}")
//    private String secreteKey;
//    private UserDtoTestService userService;
//
//
//
//    @PostConstruct
//    protected void init(){
//        secreteKey = Base64.getEncoder().encodeToString(secreteKey.getBytes());
//    }
//    public String createToken(String username){
//        Date now = new Date();
//        long expiration = 3_600_000;
//        Date expirationDate = new Date(now.getTime()+ expiration);
//        return JWT.create()
//                .withSubject(username)
//                .withIssuedAt(now)
//                .withExpiresAt(expirationDate)
//                .sign(Algorithm.HMAC256(secreteKey));
//
//    }
//
////    public Authentication validateToken(String token) {
////        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secreteKey))
////                .build();
////        DecodedJWT decodedJWT = verifier.verify(token);
////       // UserDtoTest user = userService.findByLogin(decodedJWT);
////
////        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
////
////    }
//}
