package com.example.insurance.mis.mvp.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.insurance.mis.mvp.dtos.AddAgentDto;
import com.example.insurance.mis.mvp.dtos.AddCustomerDto;
import com.example.insurance.mis.mvp.dtos.AdminDto;
import com.example.insurance.mis.mvp.dtos.AgentDto;
import com.example.insurance.mis.mvp.dtos.CustomerDto;
import com.example.insurance.mis.mvp.dtos.RedisDto;
import com.example.insurance.mis.mvp.entities.Admin;
import com.example.insurance.mis.mvp.entities.Agent;
import com.example.insurance.mis.mvp.entities.Customer;
import com.example.insurance.mis.mvp.entities.User;
import com.example.insurance.mis.mvp.repositories.AdminRepository;
import com.example.insurance.mis.mvp.repositories.AgentRepository;
import com.example.insurance.mis.mvp.repositories.CustomerRepository;
import com.example.insurance.mis.mvp.repositories.UserRepository;
import com.example.insurance.mis.mvp.util.CryptoUtil;
import com.example.insurance.mis.mvp.util.RoleEnum;

@Service
public class UserService implements UserDetailsService {

    
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AdminRepository adminRepository;

    
    @Autowired
    private AgentRepository agentRepository;

    
    @Autowired
    private CustomerRepository customerRepository;

    

    @Override
    public UserDetails loadUserByUsername(String email){
        String encryptedEmail = CryptoUtil.encrypt(email);

        User user = userRepository.findByEmail(encryptedEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getPassword())
            .authorities(user.getRole())
            .build();
    }


    public boolean addAdmin(AdminDto adminDto){
        try {

                if(adminDto.getEmail().isEmpty()){
                    throw new Exception("Admin email cannot be null");
                }

                Admin admin = new Admin();

                admin.setName(adminDto.getName());
                admin.setAge(adminDto.getAge());
                admin.setEmail(adminDto.getEmail());
                admin.setContactNo(adminDto.getContactNo());

                adminRepository.save(admin);

                User user = new User();
                
                user.setEmail(adminDto.getEmail());
                user.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                user.setRole(RoleEnum.admin.getName());
                userRepository.save(user);

                return true;
            

            
        } catch (Exception e) {
            log.error("Exception", e);
        }
       
        return false;
    }

     public boolean addAgent(AddAgentDto addAgentDto){
        try {

                
                if(addAgentDto.getAgentDto().getEmail().isEmpty()){
                    throw new Exception("Agent email cannot be null");
                }

                Agent agent = new Agent();

                agent.setName(addAgentDto.getAgentDto().getName());
                agent.setEmail(addAgentDto.getAgentDto().getEmail());
                agent.setAge(addAgentDto.getAgentDto().getAge());
                agent.setContactNo(addAgentDto.getAgentDto().getContactNo());

                agentRepository.save(agent);
                
                 User user = new User();
                
                user.setEmail(addAgentDto.getAgentDto().getEmail());
                user.setPassword(passwordEncoder.encode(addAgentDto.getAgentDto().getPassword()));
                user.setRole(RoleEnum.agent.getName());
                
                userRepository.save(user);

                return true;

            
        } catch (Exception e) {
            log.error("Exception", e);
        }
       
        return false;
    }


    public boolean addCustomer(AddCustomerDto addCustomerDto){
        try {

                
                if(addCustomerDto.getCustomerDto().getEmail().isEmpty()){
                    throw new Exception("Customer email cannot be null");
                }

                 Customer customer = new Customer();

                customer.setName(addCustomerDto.getCustomerDto().getName());
                customer.setEmail(addCustomerDto.getCustomerDto().getEmail());
                customer.setAge(addCustomerDto.getCustomerDto().getAge());
                customer.setContactNo(addCustomerDto.getCustomerDto().getContactNo());

                customerRepository.save(customer);
                
                 User user = new User();
                
                user.setEmail(addCustomerDto.getCustomerDto().getEmail());
                user.setPassword(passwordEncoder.encode(addCustomerDto.getCustomerDto().getPassword()));
                user.setRole(RoleEnum.customer.getName());
                
                userRepository.save(user);

                return true;
                
            

            
        } catch (Exception e) {
            log.error("Exception", e);
        }
       
        return false;
    }

    public Optional<AdminDto> getAdmin(String email){
        try {
             String encryptedEmail  = CryptoUtil.encrypt(email);

        Admin dbAdmin = adminRepository.findByEmail(encryptedEmail)
            .orElseThrow(() -> new Exception("No entity found"));

        User dbUser = userRepository.findByEmail(encryptedEmail)
            .orElseThrow(() -> new Exception("No entity found"));
            
        AdminDto adminDto = new AdminDto();

        adminDto.setEmail(dbAdmin.getEmail());
        adminDto.setName(dbAdmin.getName());
        adminDto.setAge(dbAdmin.getAge());
        adminDto.setContactNo(dbAdmin.getContactNo());
        adminDto.setRole(dbUser.getRole());

        return Optional.ofNullable(adminDto);
            
        } catch (Exception e) {
            log.error("Exception :: ", e);
        }
       
        return null;
    }


    public Optional<AgentDto> getAgent(String email){
        try {
            String encryptedEmail  = CryptoUtil.encrypt(email);

            Agent dbAgent = agentRepository.findByEmail(encryptedEmail)
                .orElseThrow(() -> new Exception("No entity found"));

            User dbUser = userRepository.findByEmail(encryptedEmail)
                .orElseThrow(() -> new Exception("No entity found"));
                
            AgentDto agentDto = new AgentDto();

            agentDto.setEmail(dbAgent.getEmail());
            agentDto.setName(dbAgent.getName());
            agentDto.setAge(dbAgent.getAge());
            agentDto.setContactNo(dbAgent.getContactNo());
            agentDto.setRole(dbUser.getRole());

            return Optional.ofNullable(agentDto);
                
            } catch (Exception e) {
                log.error("Exception :: ", e);
            }
        
            return null;
    }


    

    public Optional<CustomerDto> getCustomer(String email){
        try {
            String encryptedEmail  = CryptoUtil.encrypt(email);

            Customer dbCustomer = customerRepository.findByEmail(encryptedEmail)
                .orElseThrow(() -> new Exception("No entity found"));

            User dbUser = userRepository.findByEmail(encryptedEmail)
                .orElseThrow(() -> new Exception("No entity found"));
                
            CustomerDto customerDto = new CustomerDto();

            customerDto.setEmail(dbCustomer.getEmail());
            customerDto.setName(dbCustomer.getName());
            customerDto.setAge(dbCustomer.getAge());
            customerDto.setContactNo(dbCustomer.getContactNo());
            customerDto.setRole(dbUser.getRole());

            return Optional.ofNullable(customerDto);
                
            } catch (Exception e) {
                log.error("Exception :: ", e);
            }
        
            return null;
    }

    
}
