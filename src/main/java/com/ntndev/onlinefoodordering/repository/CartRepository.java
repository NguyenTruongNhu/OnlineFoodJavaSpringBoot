package com.ntndev.onlinefoodordering.repository;

import com.ntndev.onlinefoodordering.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
