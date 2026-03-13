package com.revshop.dto;

import java.time.LocalDateTime;

public class OrderHistoryDTO {
    private Long id;
    private Long buyerId;
    private Double totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private String orderNumber;
    
    // Shipping address fields
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingZipCode;
    private String shippingCountry;
    private String shippingPhone;

    public OrderHistoryDTO(Long id, Long buyerId, Double totalAmount, String status, LocalDateTime orderDate, String orderNumber) {
        this.id = id;
        this.buyerId = buyerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
    }

    // Full constructor with shipping address
    public OrderHistoryDTO(Long id, Long buyerId, Double totalAmount, String status, LocalDateTime orderDate, String orderNumber,
                           String shippingAddress, String shippingCity, String shippingState, String shippingZipCode, String shippingCountry, String shippingPhone) {
        this.id = id;
        this.buyerId = buyerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingState = shippingState;
        this.shippingZipCode = shippingZipCode;
        this.shippingCountry = shippingCountry;
        this.shippingPhone = shippingPhone;
    }

    public Long getId() {
        return id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    // Shipping address getters and setters
    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingState() {
        return shippingState;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }

    public String getShippingZipCode() {
        return shippingZipCode;
    }

    public void setShippingZipCode(String shippingZipCode) {
        this.shippingZipCode = shippingZipCode;
    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        this.shippingCountry = shippingCountry;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }
}
