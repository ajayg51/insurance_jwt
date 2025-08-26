package com.example.insurance.mis.mvp.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    
    @NotNull(message = "Product code cannot be NULL")
    @Column(unique = true)
    private String productCode;

    @NotNull(message = "Product type cannot be NULL")
    private String productType;


    @NotNull(message =  "Product expiry date cannot be NULL")
    private Date productExpiryDate;

    
    @NotNull(message =  "Product sum insured cannot be NULL")
    private Long sumInsured;


    @NotNull(message =  "Product price cannot be NULL")
    private Long productPrice;
}
