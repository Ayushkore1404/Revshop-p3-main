package com.revshop.orderservice.dto;

import com.revshop.orderservice.entity.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest {
    private OrderStatus status;
}
