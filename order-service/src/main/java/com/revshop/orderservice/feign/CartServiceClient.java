package com.revshop.orderservice.feign;

import com.revshop.orderservice.config.InternalServiceConfig;
import com.revshop.orderservice.feign.dto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cart-service", configuration = InternalServiceConfig.class, fallback = CartServiceClientFallback.class)
public interface CartServiceClient {
    
    @GetMapping("/api/cart/{userId}")
    CartResponse getCartByUserId(@PathVariable Long userId);
    
    @DeleteMapping("/api/cart/clear")
    void clearCart(@RequestParam("userId") Long userId);
}
