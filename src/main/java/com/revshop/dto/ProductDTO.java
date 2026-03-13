package com.revshop.dto;

import java.math.BigDecimal;
import com.revshop.enums.ProductCategory;

/**
 * Data Transfer Object for Product entity
 * Prevents lazy loading issues and provides clean API responses
 */
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category; // Keep as String for frontend compatibility
    private String imageUrl;
    private Integer lowStockThreshold;
    private Long sellerId;
    private String sellerName; // Additional field for UI convenience
    private String categoryDisplayName; // Display name for UI

    // Default constructor
    public ProductDTO() {}

    // Constructor with essential fields
    public ProductDTO(Long id, String name, String description, BigDecimal price, 
                      Integer stock, String category, String imageUrl, 
                      Integer lowStockThreshold, Long sellerId, String sellerName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.imageUrl = imageUrl;
        this.lowStockThreshold = lowStockThreshold;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(Integer lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getCategoryDisplayName() { return categoryDisplayName; }
    public void setCategoryDisplayName(String categoryDisplayName) { this.categoryDisplayName = categoryDisplayName; }

    // Helper method to set category from enum
    public void setCategoryFromEnum(ProductCategory category) {
        this.category = category != null ? category.getDbValue() : null;
        this.categoryDisplayName = category != null ? category.getDisplayName() : null;
    }

    // Helper method to get category as enum
    public ProductCategory getCategoryAsEnum() {
        return ProductCategory.fromDbValue(this.category);
    }
}
