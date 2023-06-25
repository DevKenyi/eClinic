//package com.example.eclinic001.service;
//
//import com.example.eclinic001.jwtConfig.UserMapper;
//import com.example.eclinic001.model.UserDtoTest;
//import com.example.eclinic001.model.UsersTest;
//import com.example.eclinic001.repo.UsersTestRepo;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class UserDtoTestService {
//    private UserMapper userMapper;
//    private UsersTestRepo repo;
//    //handle login and register
//    public UsersTest findByLogin(String login){
//        Optional<UsersTest> user = repo.findByLogin(login);
//        if(user.isPresent()){
//          return null;
//        }
//        return null;
//    }
//
//
//
//
//}
