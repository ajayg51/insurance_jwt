package com.example.insurance.mis.mvp.dtos;

import com.example.insurance.mis.mvp.entities.Product;

import lombok.Data;

@Data
public class ProductSellDto {
    private String agentId;
    private String customerId;
    private Product product;
}
