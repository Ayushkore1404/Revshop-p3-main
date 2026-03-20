package com.revshop.orderservice.feign;

import com.revshop.orderservice.feign.dto.CartResponse;
import org.springframework.stereotype.Component;

@Component
public class CartServiceClientFallback implements CartServiceClient {
    
    @Override
    public CartResponse getCartByUserId(Long userId) {
        throw new RuntimeException("Cart service is currently unavailable. Please try again later.");
    }
    
    @Override
    public void clearCart(Long userId) {
        // Log the error but don't throw exception to avoid breaking checkout flow
        System.err.println("Failed to clear cart - Cart service is unavailable");
    }
}
