package org.deep.store.services;

import org.deep.store.dtos.CartDto;
import org.deep.store.dtos.CartItemRequest;

public interface CartService {

    // add item to cart

    CartDto addItemToCart(CartItemRequest cartItemRequest, String userId);

    // remove item to cart
    void removeItemFromCart(int itemId, String userId);

    // get cart of user
    CartDto getCart(String userId);

    //
    CartDto getCartByUserEmail(String email);

}
