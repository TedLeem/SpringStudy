package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.raw.Mod;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
//@RequestMapping("/order-service")
public class OrderController {

    Environment env;
    OrderService orderService;
    KafkaProducer kafkaProducer;
    OrderProducer orderProducer;
    @Autowired
    public OrderController(Environment env,
                           OrderService orderService ,
                           KafkaProducer kafkaProducer,
                           OrderProducer orderProducer) {
        this.env = env;
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
        this.orderProducer = orderProducer;
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder requestOrder){

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(requestOrder, OrderDto.class);

//        jpa
//        orderDto.setUserId(userId);
//        orderService.createOrder(orderDto);

//        publish data to kafka connect for data synchronizaiton
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getUnitPrice()*orderDto.getQty());

//         kafka 서버를 이용한 catalog 서비스와 수량 동기화
//        kafkaProducer.send("example-category-topic",orderDto);

//        kafka connect를 이용한db에 저장 (sink connect가 메세지를 가져가 db에저장)
        orderProducer.send("orders",orderDto);

        ResponseOrder responseOrder = mapper.map(orderDto,ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId){

        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> resultList = new ArrayList<>();

        orderList.forEach(v ->{
            resultList.add(new ModelMapper().map(v,ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(resultList);

    }
}
