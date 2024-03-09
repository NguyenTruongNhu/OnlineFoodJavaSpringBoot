package com.ntndev.onlinefoodordering.repository;

import com.ntndev.onlinefoodordering.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByRestaurantId(Long id);

}
