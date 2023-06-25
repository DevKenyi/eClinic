//package com.example.eclinic001.jwtConfig;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class  JwtAuthFilter extends OncePerRequestFilter {
//    UserAuthenticationProvider userAuthenticationProvider;
//    @Override
////    protected void doFilterInternal(HttpServletRequest request,
////                                    HttpServletResponse response,
////                                    FilterChain filterChain) throws ServletException, IOException {
////       String header =  request.getHeader(HttpHeaders.AUTHORIZATION);
////       if(header!=null){
////           String[] elements = header.split("");
////           if(elements.length==2 && "Bearer".equals(elements[0])){
////               try{
////                   SecurityContextHolder.getContext().setAuthentication(
////                           //userAuthenticationProvider.validateToken(elements[1])
////                   );
////               }
////               catch (Exception e){
////                   SecurityContextHolder.clearContext();
////                   throw e;
////               }
////
////           }
////       }
////
////
//// filterChain.doFilter(request, response);
////    }
//}
