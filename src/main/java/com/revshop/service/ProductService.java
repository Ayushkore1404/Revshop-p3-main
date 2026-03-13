package com.revshop.service;

import com.revshop.dto.ProductDTO;
import com.revshop.model.Product;
import com.revshop.model.User;
import com.revshop.repository.ProductRepository;
import com.revshop.util.ProductCategoryUtil;
import com.revshop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * Convert Product entity to ProductDTO
     * Safe serialization without lazy loading issues
     */
    private ProductDTO convertToDTO(Product product) {
        return ProductCategoryUtil.convertToDTO(product);
    }

    /**
     * Get all products as DTOs
     * Safe for frontend consumption
     */
    public List<ProductDTO> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return products.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error in getAllProducts: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }

    /**
     * Get products by seller ID using CORRECT JPA relationship
     * This is the permanent fix for the seller product fetching issue
     */
    public List<ProductDTO> getProductsBySeller(Long sellerId) {
        try {
            // Validate seller exists
            if (!userRepository.existsById(sellerId)) {
                System.err.println("❌ Seller not found with ID: " + sellerId);
                return List.of();
            }
            
            // Use CORRECT JPQL query that navigates the relationship properly
            List<Product> products = productRepository.findBySellerId(sellerId);
            
            return products.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Error in getProductsBySeller: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }

    /**
     * Legacy method for backward compatibility
     * @deprecated Use getAllProducts() instead
     */
    @Deprecated
    public List<Product> getAllProductsLegacy() {
        return productRepository.findAll();
    }

    /**
     * Legacy method for backward compatibility
     * @deprecated Use getProductsBySeller(Long) instead
     */
    @Deprecated
    public List<Product> getProductsBySellerLegacy(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    /**
     * Legacy method for backward compatibility
     * @deprecated Use addProduct(ProductDTO) instead
     */
    @Deprecated
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Get product by ID as DTO
     */
    public Optional<ProductDTO> getProductById(Long productId) {
        try {
            Optional<Product> product = productRepository.findById(productId);
            return product.map(this::convertToDTO);
        } catch (Exception e) {
            System.err.println("❌ Error in getProductById: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Add new product with DTO
     */
    public ProductDTO addProductDTO(ProductDTO productDTO) {
        try {
            Product product = ProductCategoryUtil.convertToEntity(productDTO);
            
            // Set seller if provided
            if (productDTO.getSellerId() != null) {
                User seller = userRepository.findById(productDTO.getSellerId()).orElse(null);
                product.setSeller(seller);
            }
            
            Product savedProduct = productRepository.save(product);
            return convertToDTO(savedProduct);
        } catch (Exception e) {
            System.err.println("❌ Error in addProductDTO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add product: " + e.getMessage());
        }
    }

    /**
     * Update existing product
     */
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        try {
            Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
            
            // Use utility to update fields
            Product updatedProduct = ProductCategoryUtil.convertToEntity(productDTO);
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStock(updatedProduct.getStock());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());
            existingProduct.setLowStockThreshold(updatedProduct.getLowStockThreshold());
            
            Product savedProduct = productRepository.save(existingProduct);
            return convertToDTO(savedProduct);
        } catch (Exception e) {
            System.err.println("❌ Error in updateProduct: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update product: " + e.getMessage());
        }
    }

    /**
     * Delete product
     */
    public void deleteProduct(Long productId) {
        try {
            if (!productRepository.existsById(productId)) {
                throw new RuntimeException("Product not found with ID: " + productId);
            }
            productRepository.deleteById(productId);
        } catch (Exception e) {
            System.err.println("❌ Error in deleteProduct: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete product: " + e.getMessage());
        }
    }

    /**
     * Update product image URL
     */
    public void updateProductImage(Long productId, String imageUrl) {
        try {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
            
            product.setImageUrl(imageUrl);
            productRepository.save(product);
            
            System.out.println("✅ Updated image for product " + productId + ": " + imageUrl);
        } catch (Exception e) {
            System.err.println("❌ Error in updateProductImage: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update product image: " + e.getMessage());
        }
    }

    // Add other methods as needed...
}
