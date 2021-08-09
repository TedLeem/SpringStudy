package com.example.usersservice.vo;


import lombok.Data;
import lombok.Singular;
import org.springframework.context.annotation.Bean;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class RequestUser {


    @NotNull(message = "cannot be null")
    @Size(min = 2, message = "email length >=2")
    @Email(message = "email should be vailid")
    private String email;

    @NotNull(message = "cannot be null")
    @Size(min = 2, message = "name length >=2")
    private String name;

    @NotNull(message = "cannot be null")
    @Size(min = 8, message = "pwd length >=8")
    private String pwd;

}
