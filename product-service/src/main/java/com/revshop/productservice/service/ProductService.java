package com.revshop.productservice.service;

import com.revshop.productservice.dto.ProductRequest;
import com.revshop.productservice.dto.ProductResponse;
import com.revshop.productservice.entity.Product;
import com.revshop.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (!product.getIsActive()) {
            throw new RuntimeException("Product is not active");
        }
        
        return convertToResponse(product);
    }

    public List<ProductResponse> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .filter(Product::getIsActive)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerId(sellerId).stream()
                .filter(Product::getIsActive)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String search) {
        return productRepository.searchProducts(search).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProductsByCategory(String category, String search) {
        return productRepository.searchProductsByCategory(category, search).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse createProduct(ProductRequest request, Long sellerId, String sellerName) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStock(request.getStock());
        product.setDiscount(request.getDiscount() != null ? request.getDiscount() : 0.0);
        product.setSellerId(sellerId);
        product.setSellerName(sellerName);
        product.setImageUrl(request.getImageUrl());
        product.setIsActive(true);
        
        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request, Long userId, String userRole) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Check if user can update this product
        if (!userRole.equals("ADMIN") && !product.getSellerId().equals(userId)) {
            throw new RuntimeException("You can only update your own products");
        }
        
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStock(request.getStock());
        product.setDiscount(request.getDiscount() != null ? request.getDiscount() : 0.0);
        product.setImageUrl(request.getImageUrl());
        
        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public void deleteProduct(Long id, Long userId, String userRole) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Check if user can delete this product
        if (!userRole.equals("ADMIN") && !product.getSellerId().equals(userId)) {
            throw new RuntimeException("You can only delete your own products");
        }
        
        product.setIsActive(false);
        productRepository.save(product);
    }

    public void reduceStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStock(),
                product.getDiscount(),
                product.getSellerId(),
                product.getSellerName(),
                product.getImageUrl(),
                product.getIsActive(),
                product.getCreatedAt()
        );
    }
}
