package com.revshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.revshop.enums.ProductCategory;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ProductCategory category;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold;

    // CORRECT JPA RELATIONSHIP: Many-to-One with User (seller)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false, referencedColumnName = "user_id")
    @JsonIgnore // Prevent infinite recursion during JSON serialization
    private User seller;

    // Default constructor
    public Product() {}

    // Constructor with required fields
    public Product(String name, String description, BigDecimal price, Integer stock, 
                   ProductCategory category, User seller) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.seller = seller;
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

    public ProductCategory getCategory() { return category; }
    public void setCategory(ProductCategory category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(Integer lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }

    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }

    // Convenience method for backward compatibility
    public Long getSellerId() { 
        return seller != null ? seller.getId() : null; 
    }
    
    public void setSellerId(Long sellerId) { 
        if (sellerId != null) {
            this.seller = new User();
            this.seller.setId(sellerId);
        }
    }
}
