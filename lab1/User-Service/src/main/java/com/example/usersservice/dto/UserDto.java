package com.example.usersservice.dto;

import com.example.usersservice.vo.ResponseOrder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class UserDto {

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

    private String userId;

    private Date createdAt;


    private String encryptedPwd;

    private List<ResponseOrder> orders;

//    request body 에 json 형태로 있을 정보를 requestUser로 매핑한뒤 Dto로 매핑한 후 서비스에게 전달된다.
//    꼮 dto를 사용해야할 이유는 무엇일까?

}
