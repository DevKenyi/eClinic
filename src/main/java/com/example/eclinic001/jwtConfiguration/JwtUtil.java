package com.example.eclinic001.jwtConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
//    @Value("${security.jwt.toke.secret-key:secret-value}")
    private final static String SecreteKey  ="mysecretkey123";
    private final  static  long EXPIRATION_TIME = 1200000; //60 seconds

    public static String createToke(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, SecreteKey)
                .compact();
    }

    public static String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return  createToke(claims, userDetails.getUsername());
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public  static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SecreteKey).parseClaimsJws(token).getBody();
    }

    public static Long extractUserIdFromJwtToken(String token){
        Claims claims = Jwts.parser().setSigningKey( SecreteKey).parseClaimsJws(token).getBody();
        System.out.println("Claims here "+ claims);
        return Long.parseLong(claims.getSubject());
    }


}
