package com.ntndev.onlinefoodordering.controller;

import com.ntndev.onlinefoodordering.dto.request.CreateFoodRequest;
import com.ntndev.onlinefoodordering.dto.request.CreateRestaurantRequest;
import com.ntndev.onlinefoodordering.dto.response.MessageResponse;
import com.ntndev.onlinefoodordering.model.Food;
import com.ntndev.onlinefoodordering.model.Restaurant;
import com.ntndev.onlinefoodordering.model.User;
import com.ntndev.onlinefoodordering.service.FoodService;
import com.ntndev.onlinefoodordering.service.RestaurantService;
import com.ntndev.onlinefoodordering.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/create")
    public ResponseEntity<Food> createFood(
            @RequestBody CreateFoodRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        Food food = foodService.createFood(req, req.getCategory(), restaurant);

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteFood(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        foodService.deleteFood(id);

        MessageResponse res = new MessageResponse();
        res.setMessage("food deleted succesfully");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<Food> updateFoodAvaibilityStatus(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

       Food food = foodService.updateAvailibityStatus(id);


        return new ResponseEntity<>(food, HttpStatus.OK);
    }

}
