package com.example.insurance.mis.mvp.util;

public enum PolicyStatusEnum {
    active("ACTIVE"),
    inactive("INACTIVE"),
    expired("EXPIRED");

    private String name;

    PolicyStatusEnum(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
