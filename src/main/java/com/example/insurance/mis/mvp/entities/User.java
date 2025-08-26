package com.example.insurance.mis.mvp.entities;

import com.example.insurance.mis.mvp.util.CryptoUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="User_m")
public class User {
    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Email cannot be NULL")
    @Column(unique = true)
    private String email;

    
    @NotNull(message = "Password cannot be NULL")
    private String password;

    
    @NotNull(message = "Role cannot be NULL")
    private String role;
    

     @PrePersist
    @PreUpdate
    private void encryptFields(){
        this.email = CryptoUtil.encrypt(this.email);
        // this.password = CryptoUtil.encrypt(this.password);
        this.role = CryptoUtil.encrypt(this.role);
    }

    @PostLoad
    private void decryptFields(){
        this.email = CryptoUtil.decrypt(this.email);
        // this.password = CryptoUtil.decrypt(this.password);
        this.role = CryptoUtil.decrypt(this.role);
    }


}
