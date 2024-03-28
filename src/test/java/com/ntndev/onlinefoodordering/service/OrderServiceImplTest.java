package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.dto.request.OrderRequest;
import com.ntndev.onlinefoodordering.model.*;
import com.ntndev.onlinefoodordering.repository.AddressRepository;
import com.ntndev.onlinefoodordering.repository.OrderItemRepository;
import com.ntndev.onlinefoodordering.repository.OrderRepository;
import com.ntndev.onlinefoodordering.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private AddressRepository addressRepository;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private CartService cartService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Mock data
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setDeliveryAddress(new Address());
        orderRequest.setRestaurantId(1L);
        User user = new User();
        user.setId(1L);
        Address savedAddress = new Address();
//        savedAddress.setId(1L);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setFood(new Food());
        cartItem.setIngredients(new ArrayList<>());
        cartItem.setQuantity(2);
        cartItem.setTotalPrice(200L); // Assuming total price is 20.0 USD
        cartItems.add(cartItem);
        cart.setItems(cartItems);
        when(cartService.findCartByUserId(user.getId())).thenReturn(cart);

        // Mock behavior
        when(addressRepository.save(any())).thenReturn(savedAddress);
        when(restaurantService.findRestaurantById(1L)).thenReturn(restaurant);
        when(cartService.calculateCartTotals(cart)).thenReturn(100L);
        when(orderRepository.save(any())).thenReturn(new Order());
        when(orderItemRepository.save(any())).thenReturn(new OrderItem());

        // Call the method
        Order createdOrder = orderService.createOrder(orderRequest, user);

        // Verify that repository methods were called with correct parameters
        verify(addressRepository).save(any());
        verify(restaurantService).findRestaurantById(1L);
        verify(cartService).findCartByUserId(user.getId());
        verify(cartService).calculateCartTotals(cart);
        verify(orderRepository).save(any());
        verify(orderItemRepository, times(1)).save(any());

        // Verify the result
        assertNotNull(createdOrder);
        assertEquals(1L, createdOrder.getCustomer().getId());
        assertEquals("PENDING", createdOrder.getOrderStatus());
        assertEquals(savedAddress, createdOrder.getDeliveryAddress());
        assertEquals(restaurant, createdOrder.getRestaurant());
        assertEquals(100L, createdOrder.getTotalPrice());
        assertEquals(1, createdOrder.getItems().size());
    }

    @Test
    public void testFindOrderById_OrderFound() throws Exception {
        // Mock data
        Long orderId = 1L;
        Order expectedOrder = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        // Call the method
        Order foundOrder = orderService.findOrderById(orderId);

        // Verify that repository method was called with correct parameter
        verify(orderRepository).findById(orderId);

        // Verify the result
        assertNotNull(foundOrder);
        assertEquals(expectedOrder, foundOrder);
    }

    @Test
    public void testFindOrderById_OrderNotFound() {
        // Mock data
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Call the method and verify exception
        Exception exception = assertThrows(Exception.class, () -> {
            orderService.findOrderById(orderId);
        });

        // Verify the exception message
        assertEquals("Order not found", exception.getMessage());

        // Verify that repository method was called with correct parameter
        verify(orderRepository).findById(orderId);
    }
    @Test
    public void testUpdateOrder_SaveReturnsNull() {
        // Mock data
        Long orderId = 1L;
        String orderStatus = "OUT_FOR_DELIVERY";
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(null);

        // Call the method and verify exception
        Exception exception = assertThrows(Exception.class, () -> {
            orderService.updateOrder(orderId, orderStatus);
        });

        // Verify the exception message
        assertEquals("Failed to update order", exception.getMessage());

        // Verify that repository method was called with correct parameter
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(order);
    }




    @Test
    public void testUpdateOrder_InvalidStatus() {
        // Mock data
        Long orderId = 1L;
        String orderStatus = "INVALID_STATUS";
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

        // Call the method and verify exception
        Exception exception = assertThrows(Exception.class, () -> {
            orderService.updateOrder(orderId, orderStatus);
        });

        // Verify the exception message
        assertEquals("Please select a valid order status", exception.getMessage());

        // Verify that repository method was called with correct parameter
        verify(orderRepository).findById(orderId);
        // Ensure that save was not called since the status is invalid
        verify(orderRepository, never()).save(any());
    }



    @Test
    public void testCancelOrder() throws Exception {
        // Mock data
        Long orderId = 1L;

        // Call the method
        orderService.cancelOrder(orderId);

        // Verify that repository method was called with correct parameter
        verify(orderRepository).deleteById(orderId);
    }
    @Test
    public void testGetUsersOrder() throws Exception {
        // Mock data
        Long userId = 1L;
        List<Order> orders = new ArrayList<>();
        // Assuming you have some orders for the given user ID
        orders.add(new Order());
        when(orderRepository.findByCustomerId(userId)).thenReturn(orders);

        // Call the method
        List<Order> retrievedOrders = orderService.getUsersOrder(userId);

        // Verify that repository method was called with correct parameter
        verify(orderRepository).findByCustomerId(userId);

        // Verify the result
        assertNotNull(retrievedOrders);
        assertEquals(orders.size(), retrievedOrders.size());
    }

    @Test
    public void testGetRestaurantsOrder_NoStatus() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        List<Order> orders = new ArrayList<>();
        // Assuming you have some orders for the given restaurant ID
        orders.add(new Order());
        when(orderRepository.findByRestaurantId(restaurantId)).thenReturn(orders);

        // Call the method
        List<Order> retrievedOrders = orderService.getRestaurantsOrder(restaurantId, null);

        // Verify that repository method was called with correct parameter
        verify(orderRepository).findByRestaurantId(restaurantId);

        // Verify the result
        assertNotNull(retrievedOrders);
        assertEquals(orders.size(), retrievedOrders.size());
    }

    @Test
    public void testGetRestaurantsOrder_WithStatus() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        String orderStatus = "DELIVERED";
        List<Order> orders = new ArrayList<>();
        // Assuming you have some orders with the given status for the given restaurant ID
        Order order1 = new Order();
        order1.setOrderStatus(orderStatus);
        orders.add(order1);
        Order order2 = new Order();
        order2.setOrderStatus("PENDING"); // Another status
        orders.add(order2);
        when(orderRepository.findByRestaurantId(restaurantId)).thenReturn(orders);

        // Call the method
        List<Order> retrievedOrders = orderService.getRestaurantsOrder(restaurantId, orderStatus);

        // Verify that repository method was called with correct parameter
        verify(orderRepository).findByRestaurantId(restaurantId);

        // Verify the result
        assertNotNull(retrievedOrders);
        assertEquals(1, retrievedOrders.size()); // Only one order with the specified status should be returned
        assertEquals(orderStatus, retrievedOrders.get(0).getOrderStatus());
    }
}
