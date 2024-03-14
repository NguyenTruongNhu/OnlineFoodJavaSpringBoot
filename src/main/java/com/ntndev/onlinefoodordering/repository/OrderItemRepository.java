package com.ntndev.onlinefoodordering.repository;

import com.ntndev.onlinefoodordering.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
