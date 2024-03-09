package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.model.IngredientCategory;
import com.ntndev.onlinefoodordering.model.IngredientsItem;

import java.util.List;

public interface IngredientsService {
    IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception;

    IngredientCategory findIngredientCategoryById(Long id) throws Exception;

    List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception;


    IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId) throws Exception;

    List<IngredientsItem> findRestaurantsIngredients(Long restaurantId);

    IngredientsItem updateStock(Long id) throws Exception;

}
