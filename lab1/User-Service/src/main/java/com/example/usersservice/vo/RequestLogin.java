package com.example.usersservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {

    @NotNull(message = "Email cannot be null")
    @Size(min= 2 , message = "Email >= 2 ")
    @Email
    private String email;

    @NotNull(message = "password cannot be null")
    @Size(min= 8 , message = "Email >= 2 ")
    private String password;

}
