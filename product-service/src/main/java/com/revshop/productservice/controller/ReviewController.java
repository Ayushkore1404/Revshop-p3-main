package com.revshop.productservice.controller;

import com.revshop.productservice.dto.ReviewRequest;
import com.revshop.productservice.dto.ReviewResponse;
import com.revshop.productservice.service.ReviewService;
import com.revshop.productservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    @GetMapping("/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProduct(@PathVariable Long productId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long productId,
            @RequestBody ReviewRequest reviewRequest,
            @RequestHeader("Authorization") String authorization) {
        
        String token = authorization.substring(7);
        Long buyerId = Long.parseLong(jwtUtil.extractUserId(token));
        String buyerName = jwtUtil.extractEmail(token); // In real app, get from user-service
        
        ReviewResponse review = reviewService.createReview(productId, reviewRequest, buyerId, buyerName);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }
}
