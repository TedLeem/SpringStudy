package com.example.catalogservice.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCatalog {
    private String productId;

    private String productName;
//          수량
    private Integer unitPrice;

    private Integer stock;

    private Date createdAt;

}

