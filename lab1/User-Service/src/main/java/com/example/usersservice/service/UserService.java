package com.example.usersservice.service;

import com.example.usersservice.dto.UserDto;
import com.example.usersservice.hibernate.UserEntity;


public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserById(String userId);

    Iterable<UserEntity> getUserByAll();
}
