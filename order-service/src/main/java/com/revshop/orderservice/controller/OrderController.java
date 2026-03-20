package com.revshop.orderservice.controller;

import com.revshop.orderservice.dto.*;
import com.revshop.orderservice.service.OrderService;
import com.revshop.orderservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<OrderResponse> placeOrder(
        @RequestBody CheckoutRequest request,
        @RequestHeader("Authorization") String authHeader,
        HttpServletRequest httpRequest) {
        
        String token = authHeader.substring(7);
        String userIdStr = jwtUtil.extractUserId(token);
        Long userId = Long.parseLong(userIdStr);
        
        OrderResponse order = orderService.checkout(
            userId, request, token);
        return ResponseEntity.status(201).body(order);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractUserId(token));
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization) {
        
        String token = authorization.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractUserId(token));
        OrderResponse order = orderService.getOrderById(id, userId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/seller/incoming")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<List<OrderResponse>> getIncomingOrders(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractUserId(token));
        List<OrderResponse> orders = orderService.getOrdersBySellerId(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusUpdateRequest request,
            @RequestHeader("Authorization") String authorization) {
        
        String token = authorization.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractUserId(token));
        OrderResponse order = orderService.updateOrderStatus(id, request.getStatus(), userId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}/internal")
    public ResponseEntity<OrderResponse> getOrderInternal(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderByIdInternal(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/status/internal")
    public ResponseEntity<OrderResponse> updateOrderStatusInternal(
            @PathVariable Long id,
            @RequestBody OrderStatusUpdateRequest request) {
        
        OrderResponse order = orderService.updateOrderStatusInternal(id, request.getStatus());
        return ResponseEntity.ok(order);
    }
}
