package com.example.insurance.mis.mvp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.insurance.mis.mvp.dtos.LoginRequestDto;
import com.example.insurance.mis.mvp.dtos.LoginResponseDto;
import com.example.insurance.mis.mvp.entities.User;
import com.example.insurance.mis.mvp.repositories.UserRepository;
import com.example.insurance.mis.mvp.util.CryptoUtil;
import com.example.insurance.mis.mvp.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
        private final Logger log = LoggerFactory.getLogger(AuthController.class);
        
        @Autowired
        private AuthenticationManager authenticationManager;


        @Autowired
        private UserRepository userRepository;

        @Autowired
        private JwtUtil jwtUtil;

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequestDto req){
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            
            try {
                
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
                );

            } catch (BadCredentialsException ex) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Json request");
            }


            String encryptedEmail = CryptoUtil.encrypt(req.getEmail());
            User user = userRepository.findByEmail(encryptedEmail).get();
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

            loginResponseDto.setToken(token);

            return ResponseEntity.ok(loginResponseDto);
        }

       
}
