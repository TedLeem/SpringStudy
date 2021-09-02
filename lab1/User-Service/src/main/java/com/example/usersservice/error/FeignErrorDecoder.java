package com.example.usersservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    Environment env;

    @Autowired
    public FeignErrorDecoder(Environment env) {
        this.env = env;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 400:
                break;
            case 404:
                if (methodKey.contains("getOrders")){
                    return new ResponseStatusException(
                            HttpStatus.valueOf(response.status()),
                            "Users's orders is not found"
                    );
                }
                break;
            default:
                return new Exception(response.reason());
        }

        return null;
    }
}

//Feign을 이용하여 통신할때 단순한 에러처리는 FeignException에서 끝나지만
//이 클래스를 이용하여 상태값에 따라 에러 처리를 할 수 있음

//FeignErrorDecoder의 활용성 (에러코드의 제어용)
//예외처리, HTTP Resposne를 위한 상태코드와 메세지 등을 제어할 수 있다는 것은
//클라이언트 개발자에게 유요한 정보가 됌

//FeignClient를 여러개 사용할떄, 인터페이스 마다 Decoder 클래스를 만들어 적용하고 싶다면
//아래와 같이 사용
//@FeignClient(name='catalog-service", configuration = 디코더클래스명.class)
//FeignClient클래스명{~}
