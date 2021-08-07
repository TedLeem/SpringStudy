package com.example.usersservice.controller;


import com.example.usersservice.dto.UserDto;
import com.example.usersservice.service.UserService;
import com.example.usersservice.vo.RequestUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {


    Environment env;
    UserService userService;
    @Autowired
    public UserController(Environment env ,UserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @GetMapping("/health_check")
    public String status(){
        return env.getProperty("greeting.message");
    }

    @PostMapping("/users")
    public String createUser(@RequestBody RequestUser requestUser){

//        RequestBody의 역할은?
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(requestUser, UserDto.class);
        userService.createUser(userDto);

        return "create users";

    }

}
