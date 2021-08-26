package com.example.gateway.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
//    토큰을 발급받은 이후의 요청에 대해서만 이 필터를 적용해야함
//    (로그인,회원가입 요청에는 이 필터를 씌울 필요없음)
    Environment env;

    public AuthorizationHeaderFilter( Environment env) {
        super(Config.class);
        this.env = env;
    }

    public static class Config{

    }

    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
        return (exchange, chain)->{
            ServerHttpRequest request = exchange.getRequest();

            if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }
            String authorizationHeader = request.getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");
            if(!isJwtValid(jwt)){
                return onError(exchange, "Jwt token is not valid", HttpStatus.UNAUTHORIZED);
            }
            return  chain.filter(exchange);


        };
    }

    private boolean isJwtValid(String jwt){

        boolean returnValue = true;

        String subject = null;

        try{
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
//            토큰 발급할때,  token.secret을 이용하여 암호화 시켰음
//            복호화 할때 마찬가지로 token.secret을 이용함
//            복호화할 대상 jwt를 parseClaimsJws에 넣어줌
//            subject반환??? 잘모르겠음 subject


        }catch (Exception ex){
            returnValue = false;
        }
        if(subject == null || subject.isEmpty()){
            returnValue = false;

        }
        return returnValue;

    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
//        spring WebFlux를 이용하여 비동기방식으로 처리
//        Mono,Flux(데이터 단위)
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }
}
