package com.example.insurance.mis.mvp.filters;


import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.insurance.mis.mvp.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;



    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain)
            throws ServletException, IOException {


        
        log.info("JwtAuthenticationFilter triggered for " + request.getRequestURI());


        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        String role = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            username = jwtUtil.validateTokenAndGetUsername(jwtToken);
            role = jwtUtil.getRoleFromToken(jwtToken);
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
           
             List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(username, null, authorities);


            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            log.info("Username: {}", username);
            log.info("Role from token: {}", role);
            log.info("Authorities: {}", authorities);


            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}
