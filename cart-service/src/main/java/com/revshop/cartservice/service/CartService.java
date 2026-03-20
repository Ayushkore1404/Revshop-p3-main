package com.revshop.cartservice.service;

import com.revshop.cartservice.dto.*;
import com.revshop.cartservice.entity.*;
import com.revshop.cartservice.feign.ProductServiceClient;
import com.revshop.cartservice.feign.dto.ProductResponse;
import com.revshop.cartservice.repository.CartRepository;
import com.revshop.cartservice.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;

    public CartResponse getCartByUserId(Long userId) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        
        if (cartOpt.isEmpty()) {
            // Create new cart if doesn't exist
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            Cart savedCart = cartRepository.save(newCart);
            // Return empty cart response instead of throwing exception
            return new CartResponse(savedCart.getCartId(), userId, List.of(), 0.0, 0);
        }
        
        Cart cart = cartOpt.get();
        List<CartItem> items = cartItemRepository.findByCartId(cart.getCartId());
        
        // Convert to response DTOs
        List<CartItemResponse> itemResponses = items.stream()
                .map(this::convertToCartItemResponse)
                .toList();
        
        double totalAmount = itemResponses.stream()
                .mapToDouble(CartItemResponse::getItemTotal)
                .sum();
        
        int totalItems = itemResponses.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();
        
        return new CartResponse(cart.getCartId(), userId, itemResponses, totalAmount, totalItems);
    }

    public CartItemResponse addToCart(Long userId, AddToCartRequest request) {
        // Get product details from product service
        ProductResponse product = productServiceClient.getProductById(request.getProductId());
        
        if (product == null || !product.getIsActive() || product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Product not available or insufficient stock");
        }
        
        // Get or create cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
        
        // Check if product already in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), request.getProductId());
        
        if (existingItem.isPresent()) {
            // Update quantity if already exists
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(cartItem);
            return convertToCartItemResponse(cartItem);
        } else {
            // Add new item to cart
            CartItem cartItem = new CartItem();
            cartItem.setCartId(cart.getCartId());
            cartItem.setProductId(product.getProductId());
            cartItem.setProductName(product.getName());
            cartItem.setProductPrice(product.getPrice());
            cartItem.setProductImage(product.getImageUrl());
            cartItem.setQuantity(request.getQuantity());
            
            CartItem savedItem = cartItemRepository.save(cartItem);
            return convertToCartItemResponse(savedItem);
        }
    }

    public CartItemResponse updateCartItem(Long cartItemId, UpdateCartRequest request, Long userId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        // Verify ownership
        if (!cartItem.getCartId().equals(userId)) {
            throw new RuntimeException("You can only update your own cart items");
        }
        
        cartItem.setQuantity(request.getQuantity());
        CartItem updatedItem = cartItemRepository.save(cartItem);
        return convertToCartItemResponse(updatedItem);
    }

    public void removeCartItem(Long cartItemId, Long userId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        // Verify ownership
        if (!cartItem.getCartId().equals(userId)) {
            throw new RuntimeException("You can only remove your own cart items");
        }
        
        cartItemRepository.delete(cartItem);
    }

    public void clearCart(Long userId) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cartItemRepository.deleteByCartId(cart.getCartId());
        }
    }

    private CartItemResponse convertToCartItemResponse(CartItem cartItem) {
        double itemTotal = cartItem.getProductPrice() * cartItem.getQuantity();
        return new CartItemResponse(
                cartItem.getCartItemId(),
                cartItem.getProductId(),
                cartItem.getProductName(),
                cartItem.getProductPrice(),
                cartItem.getProductImage(),
                cartItem.getQuantity(),
                itemTotal
        );
    }
}
