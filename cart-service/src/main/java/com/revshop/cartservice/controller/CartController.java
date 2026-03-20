package com.revshop.cartservice.controller;

import com.revshop.cartservice.dto.*;
import com.revshop.cartservice.service.CartService;
import com.revshop.cartservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    @GetMapping
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<CartResponse> getCart(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractUserId(token));
        CartResponse cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<CartItemResponse> addToCart(
            @RequestBody AddToCartRequest request,
            @RequestHeader("Authorization") String authorization) {
        
        String token = authorization.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractUserId(token));
        CartItemResponse cartItem = cartService.addToCart(userId, request);
        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @PathVariable Long id,
            @RequestBody UpdateCartRequest request,
            @RequestHeader("Authorization") String authorization) {
        
        String token = authorization.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractUserId(token));
        CartItemResponse cartItem = cartService.updateCartItem(id, request, userId);
        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization) {
        
        String token = authorization.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractUserId(token));
        cartService.removeCartItem(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        Long userId = Long.parseLong(jwtUtil.extractUserId(token));
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId) {
        CartResponse cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }
}
