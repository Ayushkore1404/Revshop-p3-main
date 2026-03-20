package com.revshop.orderservice.feign;

import com.revshop.orderservice.config.InternalServiceConfig;
import com.revshop.orderservice.feign.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = InternalServiceConfig.class, fallback = UserServiceClientFallback.class)
public interface UserServiceClient {
    
    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable Long id);
}
