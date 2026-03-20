package com.revshop.cartservice.feign.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long productId;
    private String name;
    private Double price;
    private String imageUrl;
    private Integer stock;
    private Boolean isActive;
    private Long sellerId;
    private String sellerName;
}
