package com.example.insurance.mis.mvp.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.insurance.mis.mvp.dtos.AddAgentDto;
import com.example.insurance.mis.mvp.dtos.AddCustomerDto;
import com.example.insurance.mis.mvp.dtos.AdminDto;
import com.example.insurance.mis.mvp.dtos.AgentDto;
import com.example.insurance.mis.mvp.dtos.CustomerDto;
import com.example.insurance.mis.mvp.services.UserService;
import com.example.insurance.mis.mvp.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

// TODO : add specific status code in case failures


@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/admin/add")
    public ResponseEntity<String> addAdmin(@RequestBody AdminDto req) {
        boolean isAdded = userService.addAdmin(req);

        if(isAdded){
            return ResponseEntity.ok().body("Added admin successfully");
        }

        return ResponseEntity.badRequest().body("Failure : admin creation");

    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/agent/add")
    public ResponseEntity<String> addAgent(@RequestBody AddAgentDto req) {
        boolean isAdded = userService.addAgent(req);

        if(isAdded){
            return ResponseEntity.ok().body("Added agent successfully");
        }

        return ResponseEntity.badRequest().body("Failure : agent creation");

    }

    
    @PreAuthorize("hasRole('AGENT')")
    @PostMapping("/customer/add")
    public ResponseEntity<String> addCustomer(@RequestBody AddCustomerDto req) {
        boolean isAdded = userService.addCustomer(req);

        if(isAdded){
            return ResponseEntity.ok().body("Added customer successfully");
        }

        return ResponseEntity.badRequest().body("Failure : customer creation");

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<?> getAdmin(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String email = jwtUtil.getEmailFromToken(token);
        
        Optional<AdminDto> adminDto = userService.getAdmin(email);

        if(adminDto == null){
            return ResponseEntity.badRequest().body("Please try with admin id");
        }

        return ResponseEntity.ok().body(adminDto);

    }

    
    @PreAuthorize("hasRole('AGENT')")
    @PostMapping("/agent")
    public ResponseEntity<?> getAgent(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String email = jwtUtil.getEmailFromToken(token);
        
        
        Optional<AgentDto> agentDto = userService.getAgent(email);

        if(agentDto == null){
            return ResponseEntity.badRequest().body("Please try with agent id");
        }

        return ResponseEntity.ok().body(agentDto);

    }

    
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/customer")
    public ResponseEntity<?> getCustomer(HttpServletRequest httpServletRequest) {
            String token = httpServletRequest.getHeader("Authorization").substring(7);
            String email = jwtUtil.getEmailFromToken(token);
        
            Optional<CustomerDto> customerDto = userService.getCustomer(email);

            if(customerDto == null){
                return ResponseEntity.badRequest().body("Please try with customer id");
            }

            return ResponseEntity.ok().body(customerDto);
    }
}
