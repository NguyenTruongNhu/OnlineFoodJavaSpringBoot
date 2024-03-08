package com.ntndev.onlinefoodordering.controller;

import com.ntndev.onlinefoodordering.dto.RestaurantDto;
import com.ntndev.onlinefoodordering.dto.request.CreateRestaurantRequest;
import com.ntndev.onlinefoodordering.model.Restaurant;
import com.ntndev.onlinefoodordering.model.User;
import com.ntndev.onlinefoodordering.service.RestaurantService;
import com.ntndev.onlinefoodordering.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurant(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String keyword
            ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        List<Restaurant> restaurant = restaurantService.searchRestaurant(keyword);

        return new  ResponseEntity<>(restaurant, HttpStatus.OK);

    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Restaurant>> getAllRestaurant(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        List<Restaurant> restaurant = restaurantService.getAllRestaurant();

        return new  ResponseEntity<>(restaurant, HttpStatus.OK);

    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Restaurant> findRestaurantById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Restaurant restaurant = restaurantService.findRestaurantById(id);

        return new  ResponseEntity<>(restaurant, HttpStatus.OK);

    }

    @PutMapping("/add-favorite/{id}")
    public ResponseEntity<RestaurantDto> addToFavourites(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        RestaurantDto restaurant = restaurantService.addToFavorites(id,user);

        return new  ResponseEntity<>(restaurant, HttpStatus.OK);

    }




}
