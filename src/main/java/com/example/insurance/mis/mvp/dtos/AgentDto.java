package com.example.insurance.mis.mvp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AgentDto {
    
    private String email;

    private String name;

    private Integer age;

    private String contactNo;

    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role;
}
