package com.example.firstservice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first-service/")
@Slf4j
public class FirstServiceController {

    @GetMapping("/welcome")
    public String getWelcome(){
        return "welcome first service";
    }
    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String header){
//        first request 헤더라는 요청헤더명을 정의했었는데 거기 안에 있는 값을 header에 저장한다.
        log.info(header);
        return  "Hello World in First Service";
//        롬북 Slf4j를 사용하면 로깅을 간편하게 출력할 수 있음
    }
    @GetMapping("/check")
    public String check(){
        return "first service ";
    }
}
