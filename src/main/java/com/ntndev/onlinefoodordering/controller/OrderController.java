package com.ntndev.onlinefoodordering.controller;

import com.ntndev.onlinefoodordering.dto.request.AddCartItemRequest;
import com.ntndev.onlinefoodordering.dto.request.OrderRequest;
import com.ntndev.onlinefoodordering.model.CartItem;
import com.ntndev.onlinefoodordering.model.Order;
import com.ntndev.onlinefoodordering.model.User;
import com.ntndev.onlinefoodordering.service.OrderService;
import com.ntndev.onlinefoodordering.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {


        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.createOrder(req,user);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getOrderHistory(
            @RequestHeader("Authorization") String jwt) throws Exception {


        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.getUsersOrder(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
