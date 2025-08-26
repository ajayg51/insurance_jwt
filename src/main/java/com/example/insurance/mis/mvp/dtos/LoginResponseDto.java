package com.example.insurance.mis.mvp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class LoginResponseDto {
    
    private String token;
}
