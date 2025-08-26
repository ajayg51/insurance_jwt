package com.example.insurance.mis.mvp.util;

public enum PolicyClaimStatusEnum {
    pending("PENDING"),
    approved("APPROVED"),
    rejected("REJECTED");

    private String name;

    PolicyClaimStatusEnum(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
