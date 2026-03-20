package com.revshop.orderservice.feign;

import com.revshop.orderservice.feign.dto.StockUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceClientFallback implements ProductServiceClient {
    
    @Override
    public void reduceStock(Long id, StockUpdateRequest request) {
        // Log the error but don't throw exception to avoid breaking checkout flow
        System.err.println("Failed to reduce stock for product " + id + " - Product service is unavailable");
    }
}
