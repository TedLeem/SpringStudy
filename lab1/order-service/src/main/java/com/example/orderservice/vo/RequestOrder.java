package com.example.orderservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
public class RequestOrder {
    private String productId;

    private String productName;

    private Integer qty;

    private Integer unitPrice;

}

