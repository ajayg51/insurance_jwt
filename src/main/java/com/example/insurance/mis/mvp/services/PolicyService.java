package com.example.insurance.mis.mvp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.insurance.mis.mvp.dtos.AgentDto;
import com.example.insurance.mis.mvp.dtos.ClaimPolicyDto;
import com.example.insurance.mis.mvp.dtos.CustomerDto;
import com.example.insurance.mis.mvp.dtos.PolicyDto;
import com.example.insurance.mis.mvp.dtos.ProductDto;
import com.example.insurance.mis.mvp.dtos.ProductSellDto;
import com.example.insurance.mis.mvp.entities.Agent;
import com.example.insurance.mis.mvp.entities.Customer;
import com.example.insurance.mis.mvp.entities.Policy;
import com.example.insurance.mis.mvp.entities.Product;
import com.example.insurance.mis.mvp.entities.User;
import com.example.insurance.mis.mvp.repositories.AgentRepository;
import com.example.insurance.mis.mvp.repositories.CustomerRepository;
import com.example.insurance.mis.mvp.repositories.PolicyRepository;
import com.example.insurance.mis.mvp.repositories.ProductRepository;
import com.example.insurance.mis.mvp.repositories.UserRepository;
import com.example.insurance.mis.mvp.util.CryptoUtil;
import com.example.insurance.mis.mvp.util.JwtUtil;
import com.example.insurance.mis.mvp.util.PolicyClaimStatusEnum;
import com.example.insurance.mis.mvp.util.PolicyStatusEnum;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PolicyService {
    private final Logger log = LoggerFactory.getLogger(PolicyService.class);
    
    @Autowired
    private PolicyRepository policyRepository;

    
    @Autowired
    private ProductRepository productRepository;

    
    @Autowired
    private AgentRepository agentRepository;

    
    @Autowired
    private CustomerRepository customerRepository;

    
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    
    // @Lazy
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    

    public List<Product> getProducts(){
        List<Product> products = new ArrayList<>();

        try {
            products = productRepository.findAll();
            
            log.info("Products "+products.toString());

        } catch (Exception e) {
            log.error("Exception : ", e);
        }
        return products;
    }
    
    public void addProduct(List<ProductDto> productDtoList){
        try {
           
            for(ProductDto productDto : productDtoList){
                Product product = modelMapper.map(productDto, Product.class);
                productRepository.save(product);
            }

            log.info("Policies added successfully");
            
        } catch (Exception e) {
            log.error("Exception :: ", e);
        }
        
    }

    

    public boolean sellProduct(ProductSellDto productSellDto){
        try {
            Product product = productSellDto.getProduct();
            
            Product dbProduct = productRepository.findByProductCode(product.getProductCode())
                .orElseThrow(() -> new RuntimeException( "Product not found for product code "+ product.getProductCode()));
            

            if(! product.getProductPrice().equals( dbProduct.getProductPrice())){
                log.info("DB product price "+ dbProduct.getProductPrice());
                log.info("Sell product price "+ product.getProductPrice());

                throw new RuntimeException("Please check product price");
            }

            
            Long uniquePolicyCode = Math.abs(UUID.randomUUID().getMostSignificantBits());

            Policy policy = new Policy();
            policy.setProductCode(product.getProductCode());
            policy.setProductType(product.getProductType());
            policy.setProductExpiryDate(product.getProductExpiryDate());
            policy.setSumInsured(product.getSumInsured());
            policy.setClaimAmt(0L);
            policy.setPolicyCustomerId(productSellDto.getCustomerId());
            policy.setPolicyCode(uniquePolicyCode.toString());
            policy.setPolicySellerId(productSellDto.getAgentId());
            policy.setPolicyStatus(PolicyStatusEnum.active.getName());
            policy.setProductPrice(product.getProductPrice());
            
            policyRepository.save(policy);

            return true;
            
        } catch (Exception e) {
            log.error("Exception : ", e);
            
        }

        return false;
    }

    public List<PolicyDto> getSoldPoliciesByAgent(String email){
        List<PolicyDto> policies = new ArrayList<>();

        try {

           List<Policy> dbPolicies = policyRepository.findAllByPolicySellerId(email)
                .orElseThrow(() -> new EntityNotFoundException("Policy not found for seller id "+email));
            
            log.info(dbPolicies.toString());

            for(Policy policy : dbPolicies){
                // get customer info
                
                String encryptedEmail = CryptoUtil.encrypt(policy.getPolicyCustomerId());
                
                Customer dbCustomer = customerRepository.findByEmail(encryptedEmail)
                    .orElseThrow(() -> new EntityNotFoundException("Customer found NULL"));
                
                CustomerDto customerDto = modelMapper.map(dbCustomer, CustomerDto.class);


                PolicyDto policyDto = modelMapper.map(policy, PolicyDto.class);

                policyDto.setCustomerDto(customerDto);

                policies.add(policyDto);
            }

        } catch (Exception e) {
            log.error("Exception :: ", e);
        }

        return policies;
    }

    
    public List<PolicyDto> getSoldPolicies(){
        List<PolicyDto> policies = new ArrayList<>();

        try {
          List<Policy>  dbPolicies = policyRepository.findByPolicySellerIdIsNotNull()
                .orElseThrow(() -> new EntityNotFoundException("Policy not found"));

            for(Policy policy : dbPolicies){
                // get seller info

                String encryptedEmailSeller = CryptoUtil.encrypt(policy.getPolicySellerId());

                Agent dbAgent = agentRepository.findByEmail(encryptedEmailSeller)
                    .orElseThrow(() -> new EntityNotFoundException("Agent found NULL"));

                
                // get customer info

                String encryptedEmailCustomer = CryptoUtil.encrypt(policy.getPolicyCustomerId());

                Customer dbCustomer = customerRepository.findByEmail(encryptedEmailCustomer)
                    .orElseThrow(() -> new EntityNotFoundException("Customer found NULL"));

                
                User sellerUser = userRepository.findByEmail(encryptedEmailSeller)
                    .orElseThrow(() -> new EntityNotFoundException("seller User found NULL"));

                User customerUser = userRepository.findByEmail(encryptedEmailCustomer)
                    .orElseThrow(() -> new EntityNotFoundException("customer User found NULL"));
                
                
                
                PolicyDto policyDto = modelMapper.map(policy, PolicyDto.class);
                AgentDto agentDto = modelMapper.map(dbAgent, AgentDto.class);
                agentDto.setRole(sellerUser.getRole());

                CustomerDto customerDto = modelMapper.map(dbCustomer, CustomerDto.class);
                customerDto.setRole(customerUser.getRole());

                policyDto.setAgentDto(agentDto);
                policyDto.setCustomerDto(customerDto);
                
                policies.add(policyDto);
            }

        } catch (Exception e) {
            log.error("Exception :: ", e);
        }

        return policies;
    }

    public List<PolicyDto> getPoliciesBought(String customerId){

        List<PolicyDto> policyDtos = new ArrayList<>();

        List<Policy> dbPolicies = policyRepository.findAllByPolicyCustomerId(customerId)
            .orElseThrow(() -> new RuntimeException("Policies found NULL for customer id "+customerId));
        
        for(Policy policy : dbPolicies){
            PolicyDto policyDto = modelMapper.map(policy, PolicyDto.class);

            String encryptedSellerEmail = CryptoUtil.encrypt(policy.getPolicySellerId());

            Agent agent = agentRepository.findByEmail(encryptedSellerEmail)
                .orElseThrow(() -> new RuntimeException("Agent found NULL for seller id "+policy.getPolicySellerId()));
            
            AgentDto agentDto = modelMapper.map(agent, AgentDto.class);

            User user = userRepository.findByEmail(encryptedSellerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

            
            agentDto.setRole(user.getRole());
            
            policyDto.setAgentDto(agentDto);

            policyDtos.add(policyDto);
        }

        return policyDtos;
    }

    public boolean claimPolicy(ClaimPolicyDto claimPolicyDto){

        
        Policy policy = policyRepository.findByPolicyCode(claimPolicyDto.getPolicyCode())
            .orElseThrow(() -> new EntityNotFoundException("No policy found for id "+ claimPolicyDto.getPolicyCode()));

        if(policy.getClaimStatus() != null){
            log.error("Policy status found "+ policy.getClaimStatus());
            return false;
        }

        try {

            mailAgentCron();
            
            policy.setClaimStatus(PolicyClaimStatusEnum.pending.getName());
            policyRepository.save(policy);

            log.info("Policy scheduled to be taken action by agent id "+ claimPolicyDto.getAgentId().toString());

            return true;

        } catch (Exception e) {
            log.error("Exception ", e);
        }
        
        
        return false;
    }


    @Scheduled(cron = "${mail.agent.cron}")
    // @Scheduled(cron = "0 24 8 * * *")

    void mailAgentCron()
        throws Exception{
        
        List<Policy> dbPolicy = policyRepository.findByClaimStatus(PolicyClaimStatusEnum.pending.getName())
            .orElseThrow(() -> new EntityNotFoundException("No policy found for PENDING status"));


        try {
            for(Policy policy : dbPolicy){
                String agentId = policy.getPolicySellerId();
                String customerId = policy.getPolicyCustomerId();
                String policyCode = policy.getPolicyCode();

                String subject = customerId + " claimed policy "+policyCode;
                String body  = "Please take action on claimed policy by "+customerId;

                emailService.mailAgentRegardingClaim(agentId, subject, body);

                log.info("Mailed agent regarding claimed policy");
            } 
        } catch (Exception e) {
            log.error("Exception "+e);
        }

        
    }


    public List<Map<String, String>> claimAction(String customerId, List<PolicyDto> policyDtoList){
        List<Map<String, String>> policyActionMapList = new ArrayList<>();

        for(PolicyDto policyDto : policyDtoList){
            Policy policy = modelMapper.map(policyDto, Policy.class);
            Map<String, String> policyActionMap = new HashMap<>();
            

            Policy dbPolicy = 
                 policyRepository.findByPolicyCustomerIdAndPolicyCode(customerId, policy.getPolicyCode())
                .orElseThrow(
                    () -> new EntityNotFoundException(
                        "Policy not found for "+customerId+
                        " and policy code "+policy.getPolicyCode()
                    ));
            

            if(!dbPolicy.getClaimStatus().equals(PolicyClaimStatusEnum.pending.getName())){
                log.error("Policy not claimed by customer");
                return policyActionMapList;
            }

            long newClaimAmt = dbPolicy.getClaimAmt() + policy.getClaimAmt();
            
            if( dbPolicy.getPolicyStatus().equals(PolicyStatusEnum.active.getName()) ){
                if(policy.getPolicyClaimDate().before(dbPolicy.getProductExpiryDate())){
                    if( newClaimAmt <= dbPolicy.getSumInsured()){
                       
                        dbPolicy.setClaimAmt(newClaimAmt);
                        dbPolicy.setPolicyClaimDate(policy.getPolicyClaimDate());
                        dbPolicy.setClaimStatus(PolicyClaimStatusEnum.approved.getName());
                        
                        policyRepository.save(dbPolicy);

                        policyActionMap.put("Policy code "+policy.getPolicyCode(), PolicyClaimStatusEnum.approved.getName());
                        policyActionMapList.add(policyActionMap);


                    }else{
                        
                        dbPolicy.setClaimStatus(PolicyClaimStatusEnum.rejected.getName());
                        
                        policyRepository.save(dbPolicy);
                        
                        policyActionMap.put("Policy code "+policy.getPolicyCode(), PolicyClaimStatusEnum.rejected.getName());
                        policyActionMapList.add(policyActionMap);
                        
                        log.error("Claim amt > sum insured");
            
                    }
                }else{
                    log.error("Policy is expired");
                }
            }else{
                log.error("Policy is not active");
            }
        }

        return policyActionMapList;

    }


}
