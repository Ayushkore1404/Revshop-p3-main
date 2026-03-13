package com.revshop.util;

import com.revshop.enums.ProductCategory;
import com.revshop.model.Product;
import com.revshop.dto.ProductDTO;

/**
 * Utility class for product category conversions
 */
public class ProductCategoryUtil {
    
    /**
     * Convert Product entity to ProductDTO with proper category handling
     */
    public static ProductDTO convertToDTO(Product product) {
        if (product == null) return null;
        
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImageUrl(product.getImageUrl());
        dto.setLowStockThreshold(product.getLowStockThreshold());
        dto.setSellerId(product.getSellerId());
        
        // Handle category conversion
        if (product.getCategory() != null) {
            dto.setCategoryFromEnum(product.getCategory());
        }
        
        return dto;
    }
    
    /**
     * Convert ProductDTO to Product entity with proper category handling
     */
    public static Product convertToEntity(ProductDTO dto) {
        if (dto == null) return null;
        
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        product.setLowStockThreshold(dto.getLowStockThreshold());
        product.setSellerId(dto.getSellerId());
        
        // Handle category conversion
        if (dto.getCategory() != null && !dto.getCategory().trim().isEmpty()) {
            ProductCategory category = ProductCategory.fromDbValue(dto.getCategory().trim());
            product.setCategory(category);
        }
        
        return product;
    }
    
    /**
     * Get display name for a category string
     */
    public static String getDisplayName(String categoryDbValue) {
        if (categoryDbValue == null || categoryDbValue.trim().isEmpty()) {
            return "Uncategorized";
        }
        try {
            ProductCategory category = ProductCategory.fromDbValue(categoryDbValue.trim());
            return category.getDisplayName();
        } catch (IllegalArgumentException e) {
            return categoryDbValue; // Return original if not found
        }
    }
}
