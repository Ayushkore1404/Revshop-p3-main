package com.revshop.orderservice.feign;

import com.revshop.orderservice.feign.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserServiceClientFallback implements UserServiceClient {
    
    @Override
    public UserResponse getUserById(Long id) {
        throw new RuntimeException("User service is currently unavailable. Please try again later.");
    }
}
