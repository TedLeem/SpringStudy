package com.example.usersservice.security;

import com.example.usersservice.dto.UserDto;
import com.example.usersservice.service.UserService;
import com.example.usersservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

//사용자가 로그인을 시도하게 되면 가장 먼저 호출될 필터
//Spring Security를 이용한 로그인 요청 발생시 작업을 처리해주는 Custom FIlter 클래스
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment env;
//    토큰의 만료기간, 시크릿정보를 application.yml에 등록할 예정


    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService,
                                Environment env) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try{
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(),
                    creds.getPwd(), new ArrayList<>() ));
//             authenticationmanager가 토큰을 인증을함
//            spring security가 사용할 수 있는 형태(토큰)로 아이디와 비번을 변환한다.
        }catch(IOException e){
            throw new RuntimeException(e);

        }


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    // 인증이 성공했을떄 관련 로직을 여기에

        String userName = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(userName);

//        token 생성
//        생성된 토큰을 사용자는 어디에 저장할 것 인가?
//        jwt는 사용자 브러우저 내의 안전한 저장소 (Local Storage가아닌)곳에 저장되어야 한다.
//        jwt는 http요청으로만 서버에 전송되는 httpOnly 쿠키 내에 저장되어야 한다.
        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

//        application.yml파일에 있는 모든 값은 문자열로 가져와서 만료기간 설정은 Long형으로 바꿔줘야함

        response.addHeader("token", token);
        response.addHeader("userId" , userDetails.getUserId());
    }
}
