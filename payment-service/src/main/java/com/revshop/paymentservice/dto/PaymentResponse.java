package com.revshop.paymentservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private Long orderId;
    private Long userId;
    private Double amount;
    private String method;
    private String status;
    private String transactionId;
    private LocalDateTime createdAt;
}
