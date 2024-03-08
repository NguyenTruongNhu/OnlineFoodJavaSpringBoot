package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.dto.request.CreateFoodRequest;
import com.ntndev.onlinefoodordering.model.Category;
import com.ntndev.onlinefoodordering.model.Food;
import com.ntndev.onlinefoodordering.model.Restaurant;

import java.util.List;

public interface FoodService {

    Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant);

    void deleteFood(Long foodId) throws Exception;

    List<Food> getRestaurantsFood(Long restaurantId, boolean isVegitarain, boolean isNonveg, boolean isSeasonal, String foodCategory);

    List<Food> searchFood(String keyword);

    Food findFoodById(Long foodId) throws Exception;

    Food updateAvailibityStatus(Long foodId) throws Exception;

}
