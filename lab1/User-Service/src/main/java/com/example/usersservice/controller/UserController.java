package com.example.usersservice.controller;


import com.example.usersservice.dto.UserDto;
import com.example.usersservice.service.UserService;
import com.example.usersservice.vo.RequestUser;
import com.example.usersservice.vo.ResponseUser;
import org.apache.http.protocol.HTTP;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-service")
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
    public ResponseEntity createUser(@RequestBody RequestUser requestUser){

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(requestUser, UserDto.class);

        UserDto responseUserDto = userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(responseUserDto, ResponseUser.class);

//        보통 데이터 생성의 정상적인 응답은 201이므로 다음과 같은 response entity를 호출해준다.
        return new ResponseEntity(responseUser,HttpStatus.CREATED);

    }

}
