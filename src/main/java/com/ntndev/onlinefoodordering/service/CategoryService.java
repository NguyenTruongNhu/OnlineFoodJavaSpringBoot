package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.model.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(String name, Long userId) throws Exception;

    List<Category> findCategoryByRestaurantId(Long id) throws Exception;

    Category findCategoryById(Long id) throws Exception;
}
