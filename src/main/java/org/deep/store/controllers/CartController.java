package org.deep.store.controllers;

import org.deep.store.dtos.CartDto;
import org.deep.store.dtos.CartItemRequest;
import org.deep.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
public class CartController {

    // add item to cart
    @Autowired
    private CartService cartService;

    String userId = "021ff40b-dc03-4a9b-b290-97101b67c538";

    @PostMapping("/add-item")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        CartDto cartDto = cartService.addItemToCart(cartItemRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);

    }

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        CartDto cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);

    }

 

}
