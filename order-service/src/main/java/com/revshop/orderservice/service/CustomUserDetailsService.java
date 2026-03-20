package com.revshop.orderservice.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // This is a simple implementation for order-service
        // In a real microservice architecture, you might want to validate token
        // by calling user-service or using a shared user store
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("") // Password not needed for JWT validation
                .roles("USER")
                .build();
    }
}
