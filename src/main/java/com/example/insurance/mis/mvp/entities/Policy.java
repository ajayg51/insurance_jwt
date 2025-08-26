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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Policy")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long policyId;

    
    @Column(unique = true)
    private String policyCode;

    
    @Column(unique = true)
    private String productCode;

    private String productType;

    
    private String policyCustomerId;

    private String policySellerId;

    private Date policyClaimDate;

    private Date productExpiryDate;


    private String policyStatus;



    private Long sumInsured;

    @NotNull(message =  "Product price cannot be NULL")
    private Long productPrice;

    private String claimStatus;

    private Long claimAmt;
    

}
