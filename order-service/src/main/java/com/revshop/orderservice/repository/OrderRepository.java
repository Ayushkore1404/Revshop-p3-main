package com.revshop.orderservice.repository;

import com.revshop.orderservice.entity.Order;
import com.revshop.orderservice.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    Optional<Order> findByOrderId(Long orderId);
    
    @Query("SELECT o FROM Order o JOIN OrderItem oi ON o.orderId = oi.orderId WHERE oi.sellerId = :sellerId ORDER BY o.createdAt DESC")
    List<Order> findOrdersBySellerId(@Param("sellerId") Long sellerId);
    
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.orderId IN (SELECT oi.orderId FROM OrderItem oi WHERE oi.productId = :productId)")
    List<Order> findOrdersByProductId(@Param("productId") Long productId);
}
