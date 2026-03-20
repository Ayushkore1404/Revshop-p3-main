package com.revshop.orderservice.dto;

import com.revshop.orderservice.entity.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private String buyerName;
    private String buyerEmail;
    private String shippingAddress;
    private Double totalAmount;
    private OrderStatus status;
    private String paymentMethod;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
}
