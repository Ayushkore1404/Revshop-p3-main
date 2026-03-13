package com.revshop.controller;

import com.revshop.dto.ProductDTO;
import com.revshop.model.Product;
import com.revshop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
@Tag(name = "Product Management", description = "APIs for managing products in the RevShop e-commerce platform")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET all products (public endpoint - no authentication required)
    @GetMapping("/all")
    @Operation(summary = "Get all products (Public)", 
               description = "Retrieve all available products without authentication. This is a public endpoint for browsing products.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all products"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ProductDTO>> getAllProductsPublic() {
        try {
            System.out.println("🛍️ Public access: Fetching all products (no authentication required)");
            List<ProductDTO> products = productService.getAllProducts();
            System.out.println("📦 Retrieved " + products.size() + " products for public viewing");
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.err.println("❌ Error getting all products (public): " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts();
            System.out.println("📦 Retrieved " + products.size() + " products");
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.err.println("❌ Error getting all products: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        try {
            System.out.println("🔍 Getting product by ID: " + productId);
            
            Optional<ProductDTO> product = productService.getProductById(productId);
            if (product.isPresent()) {
                ProductDTO productDTO = product.get();
                System.out.println("✅ Found product: " + productDTO.getName() + " (Seller ID: " + productDTO.getSellerId() + ")");
                return ResponseEntity.ok(productDTO);
            } else {
                System.out.println("❌ Product not found with ID: " + productId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("❌ Error getting product by ID: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: try to get from all products
            try {
                List<ProductDTO> allProducts = productService.getAllProducts();
                for (ProductDTO p : allProducts) {
                    if (p.getId().equals(productId)) {
                        System.out.println("🔄 Found product in all products fallback: " + p.getName() + " (Seller ID: " + p.getSellerId() + ")");
                        return ResponseEntity.ok(p);
                    }
                }
                System.out.println("❌ Product not found in fallback either");
                return ResponseEntity.notFound().build();
            } catch (Exception fallbackError) {
                System.err.println("❌ Fallback also failed: " + fallbackError.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    // POST add new product
    @PostMapping
    @Operation(summary = "Add a new product", 
               description = "Create a new product in the system. Requires product details including name, price, stock, category, and seller ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully created"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try {
            // Validate product name
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Product name is required. Please enter a descriptive name for your product."
                ));
            }

            if (product.getName().trim().length() < 2) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Product name must be at least 2 characters long."
                ));
            }

            if (product.getName().trim().length() > 100) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Product name is too long. Maximum 100 characters allowed."
                ));
            }

            // Validate description
            if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Product description is required. Please describe your product to help customers understand what you're selling."
                ));
            }

            if (product.getDescription().trim().length() > 1000) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Product description is too long. Maximum 1000 characters allowed."
                ));
            }

            // Validate price
            if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Product price must be greater than 0. Please set a valid selling price."
                ));
            }

            if (product.getPrice().compareTo(new BigDecimal("1000000")) > 0) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Product price is too high. Maximum price is ₹1,000,000."
                ));
            }

            // Validate stock
            if (product.getStock() < 0) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Stock quantity cannot be negative. Please enter a valid stock number."
                ));
            }

            if (product.getStock() > 10000) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Stock quantity is too high. Maximum allowed is 10,000 items."
                ));
            }

            // Validate low stock threshold if provided
            if (product.getLowStockThreshold() != null && product.getLowStockThreshold() < 0) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Low stock threshold cannot be negative. Please enter a valid number or leave it empty to use default (5)."
                ));
            }

            if (product.getLowStockThreshold() != null && product.getLowStockThreshold() > 1000) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Low stock threshold is too high. Maximum allowed is 1,000 items."
                ));
            }

            // Validate category
            if (product.getCategory() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Product category is required. Please select a category for your product."
                ));
            }

            // Validate seller ID
            if (product.getSellerId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Seller ID is required. Please login as a seller to add products."
                ));
            }

            if (product.getSellerId() <= 0) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid seller ID. Please login again and try adding the product."
                ));
            }

            System.out.println("🔍 Adding product: " + product.getName());
            System.out.println("📦 Product details:");
            System.out.println("  - Name: " + product.getName());
            System.out.println("  - Price: " + product.getPrice());
            System.out.println("  - Stock: " + product.getStock());
            System.out.println("  - Category: " + product.getCategory());
            System.out.println("  - Seller ID: " + product.getSellerId());
            System.out.println("  - Image URL: " + product.getImageUrl());
            
            // Convert Product to ProductDTO for the service
            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setImageUrl(product.getImageUrl());
            productDTO.setSellerId(product.getSellerId());
            
            ProductDTO savedProduct = productService.addProductDTO(productDTO);
            
            System.out.println("✅ Product saved successfully:");
            System.out.println("  - ID: " + savedProduct.getId());
            System.out.println("  - Name: " + savedProduct.getName());
            System.out.println("  - Seller ID: " + savedProduct.getSellerId());
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Product added successfully!",
                    "product", savedProduct
            ));
        } catch (Exception e) {
            System.err.println("❌ Error adding product: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to add product: " + e.getMessage()
            ));
        }
    }

    // PUT update product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        try {
            System.out.println("🔍 Updating product: " + productId);
            
            // Convert Product to ProductDTO for the service
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(productId);
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setImageUrl(product.getImageUrl());
            productDTO.setSellerId(product.getSellerId());
            
            ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
            
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            System.err.println("❌ Error updating product: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE product
    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long productId) {
        try {
            System.out.println("🗑️ Deleting product: " + productId);
            
            productService.deleteProduct(productId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product deleted successfully");
            response.put("productId", productId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error deleting product: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to delete product: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // GET products by seller
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductDTO>> getProductsBySeller(@PathVariable Long sellerId) {
        System.out.println("🔍 Getting products for seller ID: " + sellerId);
        try {
            if (sellerId == null) {
                System.err.println("❌ Seller ID is null");
                return ResponseEntity.badRequest().build();
            }
            
            // Use the correct service method with DTOs
            List<ProductDTO> sellerProducts = productService.getProductsBySeller(sellerId);
            
            System.out.println("📦 Found " + sellerProducts.size() + " products for seller " + sellerId);
            
            return ResponseEntity.ok(sellerProducts);
        } catch (Exception e) {
            System.err.println("❌ Error getting seller products: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET products by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String category) {
        try {
            System.out.println("🔍 Getting products for category: " + category);
            
            List<ProductDTO> allProducts = productService.getAllProducts();
            List<ProductDTO> categoryProducts = allProducts.stream()
                .filter(product -> product.getCategory() != null && product.getCategory().equalsIgnoreCase(category))
                .collect(java.util.stream.Collectors.toList());
            
            System.out.println("📦 Found " + categoryProducts.size() + " products in category " + category);
            
            return ResponseEntity.ok(categoryProducts);
        } catch (Exception e) {
            System.err.println("❌ Error getting products by category: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET product search
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query) {
        try {
            System.out.println("🔍 Searching products with query: " + query);
            
            List<ProductDTO> allProducts = productService.getAllProducts();
            List<ProductDTO> searchResults = allProducts.stream()
                .filter(product -> 
                    (product.getName() != null && product.getName().toLowerCase().contains(query.toLowerCase())) ||
                    (product.getDescription() != null && product.getDescription().toLowerCase().contains(query.toLowerCase()))
                )
                .collect(java.util.stream.Collectors.toList());
            
            System.out.println("📦 Found " + searchResults.size() + " products matching query: " + query);
            
            return ResponseEntity.ok(searchResults);
        } catch (Exception e) {
            System.err.println("❌ Error searching products: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET product categories
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getProductCategories() {
        try {
            System.out.println("🔍 Getting product categories");
            
            List<String> categories = new ArrayList<>();
            categories.add("Electronics");
            categories.add("Clothing");
            categories.add("Books");
            categories.add("Home & Garden");
            categories.add("Sports");
            categories.add("Toys");
            
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            System.err.println("❌ Error getting product categories: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Simple test endpoint - no dependencies
    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "ProductController is running");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    // Test endpoints
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Product controller is working!");
    }

    @GetMapping("/test-no-service")
    public ResponseEntity<String> testNoService() {
        try {
            return ResponseEntity.ok("Controller works without service - no database issues");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Controller error: " + e.getMessage());
        }
    }

    // Update product image endpoint
    @PutMapping("/{productId}/image")
    public ResponseEntity<Map<String, Object>> updateProductImage(@PathVariable Long productId, @RequestBody Map<String, String> request) {
        try {
            String imageUrl = request.get("imageUrl");
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Image URL is required"
                ));
            }
            
            productService.updateProductImage(productId, imageUrl);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Product image updated successfully",
                "productId", productId,
                "imageUrl", imageUrl
            ));
        } catch (Exception e) {
            System.err.println("❌ Error updating product image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to update product image: " + e.getMessage()
            ));
        }
    }
}
