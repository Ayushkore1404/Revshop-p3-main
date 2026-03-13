package com.revshop.repository;

import com.revshop.dto.OrderHistoryDTO;
import com.revshop.model.Order;
import com.revshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByBuyer(User buyer);

    @Query("""
        SELECT new com.revshop.dto.OrderHistoryDTO(
            o.id,
            o.buyer.id,
            o.totalAmount,
            o.status,
            o.orderDate,
            o.orderNumber,
            o.shippingAddress,
            o.shippingCity,
            o.shippingState,
            o.shippingZipCode,
            o.shippingCountry,
            o.shippingPhone
        )
        FROM Order o
        WHERE o.buyer.id = :buyerId
        ORDER BY o.orderDate DESC
        """)
    List<OrderHistoryDTO> findOrderHistoryByBuyer(@Param("buyerId") Long buyerId);
}
