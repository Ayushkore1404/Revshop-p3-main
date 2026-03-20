package com.revshop.productservice.service;

import com.revshop.productservice.dto.ReviewRequest;
import com.revshop.productservice.dto.ReviewResponse;
import com.revshop.productservice.entity.Review;
import com.revshop.productservice.repository.ProductRepository;
import com.revshop.productservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public List<ReviewResponse> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ReviewResponse createReview(Long productId, ReviewRequest request, Long buyerId, String buyerName) {
        // Validate product exists
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }
        
        // Validate rating
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }
        
        // Check if user already reviewed this product
        boolean alreadyReviewed = reviewRepository.findByProductId(productId).stream()
                .anyMatch(review -> review.getBuyerId().equals(buyerId));
        
        if (alreadyReviewed) {
            throw new RuntimeException("You have already reviewed this product");
        }
        
        Review review = new Review();
        review.setProductId(productId);
        review.setBuyerId(buyerId);
        review.setBuyerName(buyerName);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        Review savedReview = reviewRepository.save(review);
        return convertToResponse(savedReview);
    }

    private ReviewResponse convertToResponse(Review review) {
        return new ReviewResponse(
                review.getReviewId(),
                review.getProductId(),
                review.getBuyerId(),
                review.getBuyerName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
