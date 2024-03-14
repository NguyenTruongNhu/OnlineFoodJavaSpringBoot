package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.dto.request.OrderRequest;
import com.ntndev.onlinefoodordering.model.Order;
import com.ntndev.onlinefoodordering.model.User;

import java.util.List;

public interface OrderService {

    Order createOrder(OrderRequest order, User user) throws Exception;

    Order updateOrder(Long orderId, String orderStatus) throws Exception;

    void cancelOrder(Long orderId) throws Exception;

    List<Order> getUsersOrder(Long userId) throws Exception;

    List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception;


    Order findOrderById(Long orderId) throws Exception;

}
