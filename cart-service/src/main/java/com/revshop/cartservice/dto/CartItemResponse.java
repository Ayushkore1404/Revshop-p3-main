package com.revshop.cartservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productImage;
    private Integer quantity;
    private Double itemTotal;
}
