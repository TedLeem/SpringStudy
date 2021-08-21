package com.example.catalogservice.messagequeue;


import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.jpa.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaConsumer {

    CatalogRepository repository;

    @Autowired
    public KafkaConsumer(CatalogRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "example-category-topic")
    public void updateQty(String kafkaMessage){
//        어노테이션에 지정된 토픽에 이벤트가 발생되면 하나 가져오는 역할을 가진 리스너
        log.info(kafkaMessage);
        System.out.println(kafkaMessage);
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try{
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });

        }catch (JsonProcessingException ex ){
            ex.printStackTrace();
        }

        CatalogEntity catalogEntity = repository.findByProductId( (String) map.get("productId") );
        if (catalogEntity!= null) {
            catalogEntity.setStock(  catalogEntity.getStock() - (Integer) map.get("qty"));
            repository.save(catalogEntity);
        }
    }

}
