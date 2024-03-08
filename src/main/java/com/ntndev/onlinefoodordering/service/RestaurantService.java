package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.dto.RestaurantDto;
import com.ntndev.onlinefoodordering.dto.request.CreateRestaurantRequest;
import com.ntndev.onlinefoodordering.model.Restaurant;
import com.ntndev.onlinefoodordering.model.User;

import java.util.List;

public interface RestaurantService {

    Restaurant createRestaurant(CreateRestaurantRequest req, User user);
    Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception;

    void deleteRestaurant(Long restaurantId) throws Exception;

    List<Restaurant> getAllRestaurant();

    List<Restaurant> searchRestaurant(String keyword);

    Restaurant findRestaurantById(Long id) throws Exception;

    Restaurant getRestaurantByUserId(Long userId) throws Exception;

    RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception;
    Restaurant updateRestaurantStatus(Long id) throws Exception;

}
