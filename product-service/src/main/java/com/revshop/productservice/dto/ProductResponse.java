package com.revshop.productservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Integer stock;
    private Double discount;
    private Long sellerId;
    private String sellerName;
    private String imageUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
