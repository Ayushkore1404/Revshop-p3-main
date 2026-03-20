package com.revshop.orderservice.feign;

import com.revshop.orderservice.config.InternalServiceConfig;
import com.revshop.orderservice.feign.dto.StockUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", configuration = InternalServiceConfig.class, fallback = ProductServiceClientFallback.class)
public interface ProductServiceClient {
    
    @PutMapping("/api/products/{id}/reduce-stock")
    void reduceStock(@PathVariable Long id, @RequestBody StockUpdateRequest request);
}
