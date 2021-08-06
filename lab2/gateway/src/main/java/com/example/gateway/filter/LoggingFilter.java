package com.example.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
//        Custom Pre Filter 적용
//        return (exchange, chain)->{
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            if (config.isPreLogger()){
//                log.info("Global Filter baseMessage: {}", config.getBaseMessage());
//            }
//
////        Custom Post Filter
//            return chain.filter(exchange).then(Mono.fromRunnable(()->{
//                if (config.isPostLogger()) {
//
//                    log.info("Global Filter End : response code -> {}", response.getStatusCode());
//                }
//            }));
//        };

        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
//            exchange: Server Request 와 ServerResponse를 사용할 수 있도록 도와주는 Instance
//            chain: 다양한 Filter를 연결시켜서 작업시킬 수 있게 해줌

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (config.isPreLogger()){
                log.info("Logging Pre Filter baseMessage: {}", config.getBaseMessage());
            }

//        Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {

                    log.info("Logging Post Filter End : response code -> {}", response.getStatusCode());
                }
            }));
            }, Ordered.LOWEST_PRECEDENCE);
        return filter;
    }

    @Data
    public static class Config{
//        put the configuration properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
        private String sayHello;
    }
}
