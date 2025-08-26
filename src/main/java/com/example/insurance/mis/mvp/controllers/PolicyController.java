package com.example.insurance.mis.mvp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.insurance.mis.mvp.dtos.ClaimPolicyDto;
import com.example.insurance.mis.mvp.dtos.ClaimsRequestDto;
import com.example.insurance.mis.mvp.dtos.PolicyDto;
import com.example.insurance.mis.mvp.dtos.ProductSellDto;
import com.example.insurance.mis.mvp.dtos.UserDto;
import com.example.insurance.mis.mvp.entities.Product;
import com.example.insurance.mis.mvp.services.PolicyService;
import com.example.insurance.mis.mvp.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

// TODO : add specific status code in case failures


@RestController
@RequestMapping("/policies")
public class PolicyController {
    private final Logger log = LoggerFactory.getLogger(PolicyController.class);
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PolicyService policyService;

    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/products/add")
    public ResponseEntity<String> addProduct(@RequestBody UserDto req){
        
        policyService.addProduct(req.getProductDtoList());
        

        return ResponseEntity.ok("Added product successfully");

    }

    
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){

        return ResponseEntity.ok().body(policyService.getProducts());
        

    }

    
    @PreAuthorize("hasRole('AGENT')")
    @PostMapping("/product/sell")
    public ResponseEntity<String> sellProduct(@RequestBody ProductSellDto req){
         
        boolean isSuccess =  policyService.sellProduct(req);
           if(isSuccess){
                 return ResponseEntity.ok("Sold policy successfully");
           }
        

        
        return ResponseEntity.badRequest().body("Please check json request");

    }

    
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    @GetMapping("/products/sold")
    public ResponseEntity<List<PolicyDto>> getSoldPolicies(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String role = jwtUtil.getRoleFromToken(token);
        
        log.info("Role from token "+role);

        List<PolicyDto> policies = new ArrayList<>();

        if(role.equals("ROLE_ADMIN")){
            policies = policyService.getSoldPolicies();

            
            return ResponseEntity.ok().body(policies);

        }else if(role.equals("ROLE_AGENT")){
                    String agentEmail  = jwtUtil.getEmailFromToken(token);
            
                    log.info("Agent email "+agentEmail);
            
                    policies = policyService.getSoldPoliciesByAgent(agentEmail);

            return ResponseEntity.ok().body(policies);
        
        }
        
        return ResponseEntity.badRequest().body(policies);
    }

    
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/claim")
    public ResponseEntity<String> claimPolicy(@RequestBody ClaimPolicyDto req){
        
            boolean isClaimedPolicy = policyService.claimPolicy(req);

            if(isClaimedPolicy)
                return ResponseEntity.ok().body("Mailed agent regarding claimed policy");
        

        return ResponseEntity.badRequest().body("Claim policy UNSUCCESSFUL");
    }

    
    
    @PreAuthorize("hasRole('AGENT')")
    @PostMapping("/claims/action")
    public ResponseEntity<List<Map<String, String>>> claimsAction(@RequestBody ClaimsRequestDto req){
        
       

        List<Map<String, String>> policyActionMapList = new ArrayList<>();

            
              policyActionMapList = 
                 policyService.claimAction(req.getCustomerId(), req.getPolicyDtoList());

             if(!policyActionMapList.isEmpty())
                return ResponseEntity.ok().body(policyActionMapList);
        


        return ResponseEntity.badRequest().body(policyActionMapList);
        

    }

   

    
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/bought")
    public ResponseEntity<List<PolicyDto>> getPoliciesBought(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String email = jwtUtil.getEmailFromToken(token);
          
       

        List<PolicyDto> policies = new ArrayList<>();

            policies = policyService.getPoliciesBought(email);

        if(!policies.isEmpty())
            return ResponseEntity.ok().body(policies);
        

        return ResponseEntity.badRequest().body(policies);

    }

    


}
