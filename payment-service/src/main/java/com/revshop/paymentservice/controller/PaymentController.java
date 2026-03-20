package com.revshop.paymentservice.controller;

import com.revshop.paymentservice.dto.PaymentRequest;
import com.revshop.paymentservice.dto.PaymentResponse;
import com.revshop.paymentservice.service.PaymentService;
import com.revshop.paymentservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/process")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN')")
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestBody PaymentRequest request,
            @RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.substring(7);
        String userIdStr = jwtUtil.extractUserId(token);
        Long userId = Long.parseLong(userIdStr);
        
        PaymentResponse response = paymentService.processPayment(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-payments")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
            @RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.substring(7);
        String userIdStr = jwtUtil.extractUserId(token);
        Long userId = Long.parseLong(userIdStr);
        
        List<PaymentResponse> payments = paymentService.getMyPayments(userId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN')")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String authHeader) {
        
        PaymentResponse payment = paymentService.getPaymentByOrderId(orderId);
        return payment != null ? ResponseEntity.ok(payment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
}
