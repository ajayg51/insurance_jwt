package com.example.insurance.mis.mvp.dtos;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDto {
    
    private Long policyId;

    private String policyCode;
    
    private String productCode;

    private String productType;

    
    private String policyCustomerId;

    private String policySellerId;

    private Date productExpiryDate;

    private Date policyClaimDate;

    private String policyStatus;

    private Long sumInsured;

    private Long productPrice;

    private String claimStatus;

    private Long claimAmt;

    // @JsonProperty("Admin details")
    // private AdminDto adminDto;

    
    @JsonProperty("Agent details")
    private AgentDto agentDto;

    
    @JsonProperty("Customer details")
    private CustomerDto customerDto;
    

}
