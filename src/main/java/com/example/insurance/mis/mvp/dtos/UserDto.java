package com.example.insurance.mis.mvp.dtos;

import java.util.List;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private List<ProductDto> productDtoList;
    private List<PolicyDto> policyDtoList;
}
