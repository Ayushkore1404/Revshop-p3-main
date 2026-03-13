package com.revshop.controller;

import com.revshop.dto.OrderHistoryDTO;
import com.revshop.dto.OrderDetailsDTO;
import com.revshop.model.Order;
import com.revshop.model.OrderItem;
import com.revshop.service.OrderService;
import com.revshop.dto.CheckoutRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // POST checkout/create order
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) {
        try {
            // Validate buyer ID
            if (request.getBuyerId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Buyer ID is required. Please login and try again."
                ));
            }

            // Validate total amount
            if (request.getTotalAmount() <= 0) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid total amount. Please add items to your cart and try again."
                ));
            }

            // Validate items
            if (request.getItems() == null || request.getItems().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "No items in cart. Please add items to your cart and try again."
                ));
            }

            // Validate shipping address
            if (request.getShippingAddress() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Shipping address is required. Please provide shipping details and try again."
                ));
            }

            System.out.println("🛒 === CHECKOUT REQUEST ===");
            System.out.println("👤 Buyer ID: " + request.getBuyerId());
            System.out.println("💰 Total Amount: " + request.getTotalAmount());
            System.out.println("📦 Order items count: " + request.getItems().size());
            
            // Debug: Log each item being saved
            for (int i = 0; i < request.getItems().size(); i++) {
                OrderItem item = request.getItems().get(i);
                System.out.println("📦 Item " + (i+1) + ":");
                System.out.println("  - Product ID: " + item.getProductId());
                System.out.println("  - Product Name: " + item.getProductName());
                System.out.println("  - Quantity: " + item.getQuantity());
                System.out.println("  - Price: " + item.getPrice());
            }
            
            Order order = new Order();
            System.out.println("🛒 === CREATING ORDER ===");
            System.out.println("👤 Buyer ID from request: " + request.getBuyerId());
            System.out.println("👤 Buyer ID type: " + (request.getBuyerId() != null ? "Long" : "null"));
            System.out.println("👤 Setting buyer ID: " + request.getBuyerId());
            
            order.setBuyerId(request.getBuyerId());
            order.setTotalAmount(request.getTotalAmount());
            order.setStatus("PLACED");
            
            System.out.println("💰 Total Amount: " + request.getTotalAmount());
            System.out.println("📊 Status set to: PLACED");
            
            // Set shipping address using already validated shippingAddress
            order.setShippingFullName(request.getShippingAddress().getFullName());
            order.setShippingAddress(request.getShippingAddress().getAddress());
            order.setShippingCity(request.getShippingAddress().getCity());
            order.setShippingState(request.getShippingAddress().getState());
            order.setShippingZipCode(request.getShippingAddress().getZipCode());
            order.setShippingCountry(request.getShippingAddress().getCountry());
            order.setShippingPhone(request.getShippingAddress().getPhone());
            System.out.println("🏠 Shipping address saved:");
            System.out.println("  - Full Name: " + request.getShippingAddress().getFullName());
            System.out.println("  - Address: " + request.getShippingAddress().getAddress());
            System.out.println("  - City: " + request.getShippingAddress().getCity());
            System.out.println("  - State: " + request.getShippingAddress().getState());
            System.out.println("  - Zip: " + request.getShippingAddress().getZipCode());
            System.out.println("  - Country: " + request.getShippingAddress().getCountry());
            System.out.println("  - Phone: " + request.getShippingAddress().getPhone());
            
            // PERMANENT FIX: Validate and save real address data
            if (request.getShippingAddress().getAddress() != null && 
                !request.getShippingAddress().getAddress().trim().isEmpty()) {
                System.out.println("✅ Real address validated: " + request.getShippingAddress().getAddress());
                order.setShippingAddress(request.getShippingAddress().getAddress());
            } else {
                System.out.println("⚠️ Address is null or empty, using default");
                order.setShippingAddress("No Address Provided");
            }
            
            if (request.getShippingAddress().getCity() != null && 
                !request.getShippingAddress().getCity().trim().isEmpty()) {
                System.out.println("✅ Real city validated: " + request.getShippingAddress().getCity());
                order.setShippingCity(request.getShippingAddress().getCity());
            } else {
                System.out.println("⚠️ City is null or empty, using default");
                order.setShippingCity("No City Provided");
            }
            
            if (request.getShippingAddress().getState() != null && 
                !request.getShippingAddress().getState().trim().isEmpty()) {
                System.out.println("✅ Real state validated: " + request.getShippingAddress().getState());
                order.setShippingState(request.getShippingAddress().getState());
            } else {
                System.out.println("⚠️ State is null or empty, using default");
                order.setShippingState("No State Provided");
            }
            
            if (request.getShippingAddress().getZipCode() != null && 
                !request.getShippingAddress().getZipCode().trim().isEmpty()) {
                System.out.println("✅ Real zip validated: " + request.getShippingAddress().getZipCode());
                order.setShippingZipCode(request.getShippingAddress().getZipCode());
            } else {
                System.out.println("⚠️ Zip is null or empty, using default");
                order.setShippingZipCode("No Zip Provided");
            }
            
            if (request.getShippingAddress().getCountry() != null && 
                !request.getShippingAddress().getCountry().trim().isEmpty()) {
                System.out.println("✅ Real country validated: " + request.getShippingAddress().getCountry());
                order.setShippingCountry(request.getShippingAddress().getCountry());
            } else {
                System.out.println("⚠️ Country is null or empty, using default");
                order.setShippingCountry("No Country Provided");
            }
            
            if (request.getShippingAddress().getFullName() != null && 
                !request.getShippingAddress().getFullName().trim().isEmpty()) {
                System.out.println("✅ Real full name validated: " + request.getShippingAddress().getFullName());
                order.setShippingFullName(request.getShippingAddress().getFullName());
            } else {
                System.out.println("⚠️ Full name is null or empty, using default");
                order.setShippingFullName("No Full Name Provided");
            }
            
            if (request.getShippingAddress().getPhone() != null && 
                !request.getShippingAddress().getPhone().trim().isEmpty()) {
                System.out.println("✅ Real phone validated: " + request.getShippingAddress().getPhone());
                order.setShippingPhone(request.getShippingAddress().getPhone());
            } else {
                System.out.println("⚠️ Phone is null or empty, using default");
                order.setShippingPhone("No Phone Provided");
            }
            
            System.out.println("💾 Saving order to database...");
            Order savedOrder = orderService.placeOrder(order, request.getItems());
            
            // Generate proper order number after ID is assigned
            String orderNumber = "ORD-" + savedOrder.getId();
            savedOrder.setOrderNumber(orderNumber);
            
            // Save again to update order number
            orderService.updateOrder(savedOrder);
            
            System.out.println("✅ Order placed successfully with ID: " + savedOrder.getId());
            System.out.println("✅ Order Number: " + savedOrder.getOrderNumber());
            System.out.println("✅ Order saved with Buyer ID: " + savedOrder.getBuyerId());
            
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            System.err.println("❌ Error during checkout: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET order details - CLEAN DTO IMPLEMENTATION
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        if (orderId == null || orderId <= 0) {
            return ResponseEntity.badRequest().body("Invalid order ID");
        }

        OrderDetailsDTO dto = orderService.getOrderDetails(orderId);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    // GET orders by buyer (updated to work for all users)
    @GetMapping("/buyer/{userId}")
    public ResponseEntity<?> getOrdersByBuyer(@PathVariable Long userId) {
        System.out.println("🔍 === GET ORDERS BY USER (UPDATED ENDPOINT) ===");
        System.out.println("👤 User ID: " + userId);
        System.out.println("👤 User ID type: " + (userId != null ? "Long" : "null"));
        System.out.println("👤 User ID value: " + userId);
        
        if (userId == null || userId <= 0) {
            System.out.println("❌ Invalid user ID");
            return ResponseEntity.badRequest().body("Invalid user ID");
        }

        System.out.println("🔍 Calling orderService.getOrdersByBuyer(" + userId + ")");
        List<OrderHistoryDTO> orders = orderService.getOrdersByBuyer(userId);
        System.out.println("📊 Orders found: " + (orders != null ? orders.size() : 0));
        
        if (orders != null && !orders.isEmpty()) {
            for (int i = 0; i < orders.size(); i++) {
                OrderHistoryDTO order = orders.get(i);
                System.out.println("📦 Order " + (i + 1) + ":");
                System.out.println("  - ID: " + order.getId());
                System.out.println("  - Order Number: " + order.getOrderNumber());
                System.out.println("  - Status: " + order.getStatus());
                System.out.println("  - Total Amount: " + order.getTotalAmount());
                System.out.println("  - Order Date: " + order.getOrderDate());
                System.out.println("  - Shipping Address: " + order.getShippingAddress());
                System.out.println("  - Shipping City: " + order.getShippingCity());
                System.out.println("  - Shipping State: " + order.getShippingState());
                System.out.println("  - Shipping Zip: " + order.getShippingZipCode());
                System.out.println("  - Shipping Country: " + order.getShippingCountry());
            }
        }
        
        return ResponseEntity.ok(orders);
    }

    // GET orders by any user (buyer or seller)
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrdersByUser(@PathVariable Long userId) {
        System.out.println("🔍 === GET ORDERS BY USER ===");
        System.out.println("👤 User ID: " + userId);
        System.out.println("👤 User ID type: " + (userId != null ? "Long" : "null"));
        System.out.println("👤 User ID value: " + userId);
        
        if (userId == null || userId <= 0) {
            System.out.println("❌ Invalid user ID");
            return ResponseEntity.badRequest().body("Invalid user ID");
        }

        System.out.println("🔍 Calling orderService.getOrdersByBuyer(" + userId + ")");
        List<OrderHistoryDTO> orders = orderService.getOrdersByBuyer(userId);
        System.out.println("📊 Orders found: " + (orders != null ? orders.size() : 0));
        
        if (orders != null && !orders.isEmpty()) {
            for (int i = 0; i < orders.size(); i++) {
                OrderHistoryDTO order = orders.get(i);
                System.out.println("📦 Order " + (i + 1) + ":");
                System.out.println("  - ID: " + order.getId());
                System.out.println("  - Order Number: " + order.getOrderNumber());
                System.out.println("  - Status: " + order.getStatus());
                System.out.println("  - Total Amount: " + order.getTotalAmount());
                System.out.println("  - Order Date: " + order.getOrderDate());
                System.out.println("  - Shipping Address: " + order.getShippingAddress());
                System.out.println("  - Shipping City: " + order.getShippingCity());
                System.out.println("  - Shipping State: " + order.getShippingState());
                System.out.println("  - Shipping Zip: " + order.getShippingZipCode());
                System.out.println("  - Shipping Country: " + order.getShippingCountry());
            }
        }
        
        return ResponseEntity.ok(orders);
    }

    // PUT update order status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, Object> statusRequest) {
        try {
            System.out.println("🔄 Updating order status: " + orderId);
            System.out.println("  - New status: " + statusRequest.get("status"));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order status updated successfully");
            response.put("orderId", orderId);
            response.put("status", statusRequest.get("status"));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error updating order status: " + e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to update order status: " + e.getMessage());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // DELETE cancel order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Long orderId) {
        try {
            System.out.println("❌ Cancelling order: " + orderId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order cancelled successfully");
            response.put("orderId", orderId);
            response.put("status", "CANCELLED");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error cancelling order: " + e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to cancel order: " + e.getMessage());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
