package com.example.insurance.mis.mvp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.insurance.mis.mvp.filters.JwtAuthenticationFilter;
import com.example.insurance.mis.mvp.util.JwtUtil;
import com.example.insurance.mis.mvp.util.RoleEnum;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityConfig {

    @Autowired
    private  JwtUtil jwtUtil;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }


    @Bean
    public SecurityFilterChain securityFilterChain
        (HttpSecurity httpSecurity) throws Exception{


            httpSecurity.csrf(
                csrf -> csrf.disable()
            )
            
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(
                auth -> auth.requestMatchers(
                    "/auth/register",
                    "/auth/login",
                    "/user/admin/add"
                ).permitAll()
                .requestMatchers(
                    "/policies/products",
                    
                    "/policies/products/sold"
                ).hasAnyRole(
                    RoleEnum.admin.getName(),
                    RoleEnum.agent.getName()
                )
                .requestMatchers(
                    "/user/admin",
                     "/user/agent/add",
                    "/policies/products/add"
                ).hasRole(RoleEnum.admin.getName())
                .requestMatchers(
                    "/user/agent",
                    "/user/customer/add",
                    "/policies/claims/action",
                    "/policies/product/sell"
                ).hasRole(RoleEnum.agent.getName())
                .requestMatchers(
                    "/user/customer",
                    "/policies/bought",
                    "/policies/claim"
                ).hasRole(RoleEnum.customer.getName())
                .anyRequest().authenticated()
            ) .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            



            return httpSecurity.build();    

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public AuthenticationManager authenticationManager(
        HttpSecurity http, 
        PasswordEncoder passwordEncoder, 
        UserDetailsService userDetailsService)
        throws Exception {

            AuthenticationManagerBuilder authBuilder = 
                http.getSharedObject(AuthenticationManagerBuilder.class);
            
            authBuilder.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder);
            
            return authBuilder.build();

        
        }


}
