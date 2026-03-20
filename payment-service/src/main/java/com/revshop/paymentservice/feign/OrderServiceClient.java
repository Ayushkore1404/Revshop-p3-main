package com.revshop.paymentservice.feign;

import com.revshop.paymentservice.feign.dto.OrderResponse;
import com.revshop.paymentservice.feign.dto.OrderStatusUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service")
public interface OrderServiceClient {
    
    @GetMapping("/api/orders/{id}/internal")
    OrderResponse getOrderById(@PathVariable Long id);
    
    @PutMapping("/api/orders/{id}/status/internal")
    OrderResponse updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusUpdateRequest request);
}
