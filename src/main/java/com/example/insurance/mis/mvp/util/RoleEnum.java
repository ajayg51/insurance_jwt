package com.example.insurance.mis.mvp.util;

public enum RoleEnum {
    customer("CUSTOMER"),
    agent("AGENT"),
    admin("ADMIN");

    private String name;

    RoleEnum(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
