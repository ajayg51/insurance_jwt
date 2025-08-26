package com.example.insurance.mis.mvp.dtos;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    
    private Long productId;
    
    private String productCode;

    private String productType;


    private Date productExpiryDate;

    private Long sumInsured;

    private Long productPrice;
}
