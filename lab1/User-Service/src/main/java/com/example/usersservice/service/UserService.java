package com.example.usersservice.service;

import com.example.usersservice.dto.UserDto;
import com.example.usersservice.hibernate.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserById(String userId);

    Iterable<UserEntity> getUserByAll();
}
