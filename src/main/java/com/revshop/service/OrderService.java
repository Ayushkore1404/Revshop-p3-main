package com.revshop.service;

import com.revshop.dto.OrderHistoryDTO;
import com.revshop.dto.OrderDetailsDTO;
import com.revshop.dto.OrderItemDTO;
import com.revshop.model.Order;
import com.revshop.model.OrderItem;
import com.revshop.repository.OrderItemRepository;
import com.revshop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order placeOrder(Order order, List<OrderItem> items) {
        Order savedOrder = orderRepository.save(order);
        for (OrderItem item : items) {
            item.setOrderId(savedOrder.getId());
            orderItemRepository.save(item);
        }
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<OrderHistoryDTO> getOrdersByBuyer(Long buyerId) {
        System.out.println("🔍 === ORDER SERVICE GET ORDERS BY BUYER ===");
        System.out.println("👤 Buyer ID: " + buyerId);
        System.out.println("👤 Buyer ID type: " + (buyerId != null ? "Long" : "null"));
        System.out.println("👤 Buyer ID value: " + buyerId);
        
        List<OrderHistoryDTO> orders = orderRepository.findOrderHistoryByBuyer(buyerId);
        System.out.println("📊 Repository returned: " + (orders != null ? orders.size() + " orders" : "null"));
        
        return orders;
    }

    @Transactional(readOnly = true)
    public OrderDetailsDTO getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        List<OrderItemDTO> itemDTOs = new ArrayList<>();

        for (OrderItem item : orderItems) {
            OrderItemDTO itemDTO = new OrderItemDTO(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getImageUrl(),
                item.getQuantity(),
                item.getPrice()
            );
            itemDTOs.add(itemDTO);
        }

        return new OrderDetailsDTO(
            order.getId(),
            order.getBuyerId(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getOrderDate(),
            order.getOrderNumber(),
            order.getShippingFullName(),
            order.getShippingAddress(),
            order.getShippingCity(),
            order.getShippingState(),
            order.getShippingZipCode(),
            order.getShippingCountry(),
            order.getShippingPhone(),
            itemDTOs
        );
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void placeOrderItem(OrderItem item) {
        orderItemRepository.save(item);
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
}
