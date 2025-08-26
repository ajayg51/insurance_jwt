package com.example.insurance.mis.mvp.entities;

import com.example.insurance.mis.mvp.util.CryptoUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    
    @NotNull(message = "Email cannot be NULL")
    private String email;

    @NotNull(message = "Name cannot be NULL")
    private String name;

    @NotNull(message = "Age cannot be NULL")
    private Integer age;

    @NotNull(message = "Contact no. cannot be NULL")
    private String contactNo;

    

    @PrePersist
    @PreUpdate
    private void encryptFields(){
        this.name = CryptoUtil.encrypt(this.name);
        this.email = CryptoUtil.encrypt(this.email);
        this.contactNo = CryptoUtil.encrypt(this.contactNo);
    }

    @PostLoad
    private void decryptFields(){
        this.name = CryptoUtil.decrypt(this.name);
        this.email = CryptoUtil.decrypt(this.email);
        this.contactNo = CryptoUtil.decrypt(this.contactNo);
    }

}
