package com.example.usersservice.security;

import com.example.usersservice.service.UserService;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;


// 스프링 앱이 실행되면 컨피규레이션 어노테이션이 붙여져있는 클래스가 메모리에 올라가 빈으로 등록된다.
// Configuration이 붙은 클래스는 애플리케이션 컨텍스트가 Ioc적용을 위한 설정정보이다.

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 인증이 통과가 되면 할 수 있는 작업들인 권한과 관련된 설정
        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests().antMatchers("/**")
                .hasIpAddress("127.0.0.1")
                .and()
                .addFilter(getAuthenticationFilter());
//        getAuthenticationFilter ㅔ소드를 통해


        http.headers().frameOptions().disable();

    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter
                = new AuthenticationFilter(authenticationManager() , userService, env);

        authenticationFilter.setAuthenticationManager(authenticationManager());
        return authenticationFilter;
    // AuthenticationManager는 ProvierManager를 구현한 클래스로써,
    // 인자로 전달받은 유저에 대한 인증 정보를 담고 있으며, 해당 인증 정보가 유효할 경우
    // UserDetailsService에서 적절한 Principal을 가지고 있는 Authentication 객체를 반환해 주는 역할을
    // 하는 인증 공급자(Provider) 입니다.

//        스프링 시큐리티에 있는 authenticationManager를 등록해 스프링시큐리티 로그인을 사용하려함
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
// 인증과 관련된 설정
// 인증이 되어야지 권한부여받을 수 있음
//   BCryptPasswordEncoder는 BCrypt 해싱 함수를 사용해서 데이터를 암호화하는 클래스
//        Bcrypt는 데이터(주로 패스워드)를 해싱할 떄 내부적으로 랜덤한 솔트를 생성하기 떄문에 같은 문자열에 대해서도
//        다른 결과를 생성해 냅니다. 같은 데이터를 암호화 하였을때, 매번 같은 데이터가 발생하면, 누구나 쉽게 데이터를 복호화 할 수 도 있을 겁니다.
//
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        super.configure(auth);
    }
}
