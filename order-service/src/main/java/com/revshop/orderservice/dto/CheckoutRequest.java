package com.revshop.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    private Long buyerId;
    private Double totalAmount;
    private String shippingAddress;
    private String paymentMethod;
    private List<OrderItemRequest> orderItems;
}
