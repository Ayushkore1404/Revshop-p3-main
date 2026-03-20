package com.revshop.productservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long reviewId;
    private Long productId;
    private Long buyerId;
    private String buyerName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
