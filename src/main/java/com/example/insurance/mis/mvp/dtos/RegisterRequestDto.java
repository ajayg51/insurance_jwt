package com.example.insurance.mis.mvp.dtos;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String email;
    private String password;
    private String role;
}
