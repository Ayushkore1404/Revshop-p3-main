package com.revshop.orderservice.service;

import com.revshop.orderservice.dto.*;
import com.revshop.orderservice.entity.*;
import com.revshop.orderservice.feign.CartServiceClient;
import com.revshop.orderservice.feign.ProductServiceClient;
import com.revshop.orderservice.feign.UserServiceClient;
import com.revshop.orderservice.feign.dto.*;
import com.revshop.orderservice.repository.OrderRepository;
import com.revshop.orderservice.repository.OrderItemRepository;
import com.revshop.orderservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartServiceClient cartServiceClient;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    
    @Autowired
    private JwtUtil jwtUtil;

    public OrderResponse checkout(Long userId, CheckoutRequest request, String token) {
        try {
            // 1. Extract user details from JWT token directly - no Feign call needed
            String buyerName = jwtUtil.extractName(token);
            String buyerEmail = jwtUtil.extractEmail(token);
            
            System.out.println("🛒 Checkout request received:");
            System.out.println("👤 User ID: " + userId);
            System.out.println("👤 Buyer Name: " + buyerName);
            System.out.println("📧 Buyer Email: " + buyerEmail);
            System.out.println("📦 Total Amount: " + request.getTotalAmount());
            System.out.println("🏠 Shipping Address: " + request.getShippingAddress());
            System.out.println("💳 Payment Method: " + request.getPaymentMethod());
            System.out.println("🛒 Order Items: " + (request.getOrderItems() != null ? request.getOrderItems().size() : 0));
            
            // Log request details
            if (request.getOrderItems() != null) {
                request.getOrderItems().forEach(item -> {
                    System.out.println("  - Item: " + item.getProductId() + 
                           ", Quantity: " + item.getQuantity() + 
                           ", Price: " + item.getPrice() + 
                           ", Name: " + item.getName());
                });
            }

            // 2. Get cart items
            CartResponse cart = cartServiceClient.getCartByUserId(userId);
            System.out.println("🛒 Cart response: " + cart);
            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
                throw new RuntimeException("Cart is empty");
            }

            // 3. Create order
            Order order = new Order();
            order.setUserId(userId);
            order.setBuyerName(buyerName);
            order.setBuyerEmail(buyerEmail);
            order.setShippingAddress(request.getShippingAddress());
            order.setPaymentMethod(request.getPaymentMethod());
            order.setStatus(OrderStatus.PENDING);

            // 4. Create order items and calculate total
            double totalAmount = 0.0;
            for (CartItemResponse cartItem : cart.getItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductName(cartItem.getProductName());
                orderItem.setProductPrice(cartItem.getProductPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setItemTotal(cartItem.getItemTotal());
                orderItem.setSellerId(
                    cartItem.getSellerId() != null ? 
                    cartItem.getSellerId() : 0L);
                
                totalAmount += cartItem.getItemTotal();
            }
            
            order.setTotalAmount(totalAmount);
            
            // 5. Save order and order items
            Order savedOrder = orderRepository.save(order);
            
            for (CartItemResponse cartItem : cart.getItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(savedOrder.getOrderId());
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductName(cartItem.getProductName());
                orderItem.setProductPrice(cartItem.getProductPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setItemTotal(cartItem.getItemTotal());
                orderItem.setSellerId(
                    cartItem.getSellerId() != null ? 
                    cartItem.getSellerId() : 0L);
                
                orderItemRepository.save(orderItem);
                
                // 6. Reduce stock for each product
                try {
                    StockUpdateRequest stockRequest = new StockUpdateRequest(cartItem.getQuantity());
                    productServiceClient.reduceStock(cartItem.getProductId(), stockRequest);
                } catch (Exception e) {
                    // Log error but continue with order creation
                    System.err.println("Failed to reduce stock for product " + cartItem.getProductId() + ": " + e.getMessage());
                }
            }
            
            // 7. Clear cart
            try {
                cartServiceClient.clearCart(userId);
            } catch (Exception e) {
                // Log error but continue
                System.err.println("Failed to clear cart: " + e.getMessage());
            }
            
            return convertToOrderResponse(savedOrder);
            
        } catch (Exception e) {
            throw new RuntimeException("Checkout failed: " + e.getMessage());
        }
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Verify ownership
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
        
        return convertToOrderResponse(order);
    }

    public List<OrderResponse> getOrdersBySellerId(Long sellerId) {
        List<Order> orders = orderRepository.findOrdersBySellerId(sellerId);
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status, Long userId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Verify seller owns items in this order
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        boolean isSeller = orderItems.stream().anyMatch(item -> item.getSellerId().equals(userId));
        
        if (!isSeller) {
            throw new RuntimeException("Access denied");
        }
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return convertToOrderResponse(updatedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderByIdInternal(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToOrderResponse(order);
    }

    public OrderResponse updateOrderStatusInternal(Long orderId, OrderStatus status) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return convertToOrderResponse(updatedOrder);
    }

    private OrderResponse convertToOrderResponse(Order order) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(this::convertToOrderItemResponse)
                .collect(Collectors.toList());
        
        return new OrderResponse(
                order.getOrderId(),
                order.getUserId(),
                order.getBuyerName(),
                order.getBuyerEmail(),
                order.getShippingAddress(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getPaymentMethod(),
                itemResponses,
                order.getCreatedAt()
        );
    }

    private OrderItemResponse convertToOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getOrderItemId(),
                orderItem.getProductId(),
                orderItem.getProductName(),
                orderItem.getProductPrice(),
                orderItem.getQuantity(),
                orderItem.getItemTotal(),
                orderItem.getSellerId()
        );
    }
}
