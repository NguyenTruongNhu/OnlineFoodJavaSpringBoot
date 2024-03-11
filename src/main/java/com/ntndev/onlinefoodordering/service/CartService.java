package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.dto.request.AddCartItemRequest;
import com.ntndev.onlinefoodordering.model.Cart;
import com.ntndev.onlinefoodordering.model.CartItem;

public interface CartService {

        CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception;
        CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception;
        Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception;

        Long calculateCartTotals(Cart cart) throws Exception;

        Cart findCartById(Long id) throws Exception;

        Cart findCartByUserId(String jwt) throws Exception;

        Cart clearCart(String jwt) throws Exception;


}
