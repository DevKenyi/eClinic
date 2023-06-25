package com.example.eclinic001.jwtConfig;

import com.example.eclinic001.model.UserDtoTest;
import com.example.eclinic001.model.UsersTest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    UserDtoTest toUserDto(UsersTest usersTest);
}
