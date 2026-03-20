package com.revshop.paymentservice.service;

import com.revshop.paymentservice.dto.PaymentRequest;
import com.revshop.paymentservice.dto.PaymentResponse;
import com.revshop.paymentservice.entity.Payment;
import com.revshop.paymentservice.enums.PaymentMethod;
import com.revshop.paymentservice.enums.PaymentStatus;
import com.revshop.paymentservice.feign.OrderServiceClient;
import com.revshop.paymentservice.feign.dto.OrderResponse;
import com.revshop.paymentservice.feign.dto.OrderStatusUpdateRequest;
import com.revshop.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderServiceClient orderServiceClient;

    public PaymentResponse processPayment(Long userId, PaymentRequest request) {
        // 1. Generate UUID transactionId
        String transactionId = UUID.randomUUID().toString();
        
        // 2. Simulate payment
        PaymentStatus status;
        PaymentMethod method = PaymentMethod.valueOf(request.getMethod().toUpperCase());
        
        if (method == PaymentMethod.COD) {
            status = PaymentStatus.SUCCESS;
        } else {
            // CREDIT_CARD or DEBIT_CARD -> 90% success rate
            status = Math.random() > 0.1 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
        }
        
        // 3. Save payment record
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(userId);
        payment.setAmount(request.getAmount());
        payment.setMethod(method);
        payment.setStatus(status);
        payment.setTransactionId(transactionId);
        
        Payment savedPayment = paymentRepository.save(payment);
        
        // 4. If SUCCESS -> call OrderServiceClient to update order status to CONFIRMED
        if (status == PaymentStatus.SUCCESS) {
            try {
                OrderStatusUpdateRequest updateRequest = new OrderStatusUpdateRequest();
                updateRequest.setStatus("CONFIRMED");
                orderServiceClient.updateOrderStatus(request.getOrderId(), updateRequest);
            } catch (Exception e) {
                System.err.println("Failed to update order status: " + e.getMessage());
            }
        }
        
        // 5. Return PaymentResponse
        return convertToPaymentResponse(savedPayment);
    }
    
    public List<PaymentResponse> getMyPayments(Long userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        return payments.stream()
                .map(this::convertToPaymentResponse)
                .toList();
    }
    
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId);
        return payment != null ? convertToPaymentResponse(payment) : null;
    }
    
    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(this::convertToPaymentResponse)
                .toList();
    }
    
    private PaymentResponse convertToPaymentResponse(Payment payment) {
        return new PaymentResponse(
            payment.getPaymentId(),
            payment.getOrderId(),
            payment.getUserId(),
            payment.getAmount(),
            payment.getMethod().toString(),
            payment.getStatus().toString(),
            payment.getTransactionId(),
            payment.getCreatedAt()
        );
    }
}
