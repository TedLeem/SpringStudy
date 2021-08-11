package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    public CustomFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
//        Custom Pre Filter 적용
        return (exchange, chain)->{
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
//      response값을 미리 알수있나?? Postfilet뒤에 알아야 되는거아닌가?
            log.info("Custom PRE filter : request id -> {}", request.getId());
//        Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("Custom Post filter : response id -> {}", response.getStatusCode());
            }));
        };
    }
//    exchange chain 다시 이해하기

    public static class Config{
//        put the configuration properties
    }
}
