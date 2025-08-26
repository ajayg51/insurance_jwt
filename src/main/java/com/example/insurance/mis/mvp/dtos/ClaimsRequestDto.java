package com.example.insurance.mis.mvp.dtos;

import java.util.List;

import lombok.Data;

@Data
public class ClaimsRequestDto {
    private String customerId;
    private List<PolicyDto> policyDtoList;
}
