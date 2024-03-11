package com.ntndev.onlinefoodordering.repository;

import com.ntndev.onlinefoodordering.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
