package com.example.orderservice.messagequeue;

import com.example.orderservice.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class OrderProducer {
    private KafkaTemplate<String ,String> kafkaTemplate;

// Kafka sink connect가 인식할 수 있는 형식인 schema,field,payload 형태로 만들어준다.

    private List<Field> fields = Arrays.asList(new Field("string",true,"order_id"),
            new Field("string",true,"user_id"),
            new Field("string",true,"product_id"),
            new Field("int32",true,"qty"),
            new Field("int32",true,"unit_price"),
            new Field("int32",true,"total_price"));

    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("orders")
            .build();

    @Autowired
    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public OrderDto send(String topic, OrderDto orderDto) {

        Payload payload = Payload.builder()
                .order_id(orderDto.getOrderId())
                .user_id(orderDto.getUserId())
                .product_id(orderDto.getProductId())
                .qty(orderDto.getQty())
                .unit_price(orderDto.getUnitPrice())
                .total_price(orderDto.getTotalPrice())
                .build();

        KafkaOrderDto kafkaOrderDto = KafkaOrderDto.builder()
                .schema(schema)
                .payload(payload)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try{
            jsonInString = mapper.writeValueAsString(kafkaOrderDto);

        }catch (JsonProcessingException ex){
            ex.printStackTrace();

        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Order Producer sent data from the ORder microservice" + kafkaOrderDto);

        return orderDto;
    }

}
