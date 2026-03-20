package com.revshop.productservice.controller;

import com.revshop.productservice.dto.ProductRequest;
import com.revshop.productservice.dto.ProductResponse;
import com.revshop.productservice.dto.StockUpdateRequest;
import com.revshop.productservice.entity.Product;
import com.revshop.productservice.service.ProductService;
import com.revshop.productservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        
        List<ProductResponse> products;
        
        if (category != null && search != null) {
            products = productService.searchProductsByCategory(category, search);
        } else if (category != null) {
            products = productService.getProductsByCategory(category);
        } else if (search != null) {
            products = productService.searchProducts(search);
        } else {
            products = productService.getAllActiveProducts();
        }
        
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/seller/{sellerId}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<List<ProductResponse>> getProductsBySeller(@PathVariable Long sellerId) {
        List<ProductResponse> products = productService.getProductsBySeller(sellerId);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest productRequest,
            @RequestHeader("Authorization") String authorization) {
        
        String token = authorization.substring(7);
        Long sellerId = Long.parseLong(jwtUtil.extractUserId(token));
        String sellerName = jwtUtil.extractEmail(token); // In real app, get from user-service
        
        ProductResponse product = productService.createProduct(productRequest, sellerId, sellerName);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest productRequest,
            @RequestHeader("Authorization") String authorization) {
        
        String token = authorization.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractUserId(token));
        String userRole = jwtUtil.extractRole(token);
        
        ProductResponse product = productService.updateProduct(id, productRequest, userId, userRole);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization) {
        
        String token = authorization.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractUserId(token));
        String userRole = jwtUtil.extractRole(token);
        
        productService.deleteProduct(id, userId, userRole);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reduce-stock")
    public ResponseEntity<Void> reduceStock(
            @PathVariable Long id,
            @RequestBody StockUpdateRequest stockUpdate) {
        
        productService.reduceStock(id, stockUpdate.getQuantity());
        return ResponseEntity.ok().build();
    }
}
