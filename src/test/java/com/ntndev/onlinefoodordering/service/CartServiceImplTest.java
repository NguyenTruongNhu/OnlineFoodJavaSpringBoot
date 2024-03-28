package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.dto.request.AddCartItemRequest;
import com.ntndev.onlinefoodordering.model.Cart;
import com.ntndev.onlinefoodordering.model.CartItem;
import com.ntndev.onlinefoodordering.model.Food;
import com.ntndev.onlinefoodordering.model.User;
import com.ntndev.onlinefoodordering.repository.CartItemRepository;
import com.ntndev.onlinefoodordering.repository.CartRepository;
import org.aspectj.lang.annotation.Before;
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
public class CartServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private FoodService foodService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }
    @Test
    public void testAddItemToCart_NewCartItem() throws Exception {
        // Tạo dữ liệu mô phỏng
        AddCartItemRequest req = new AddCartItemRequest();
        req.setFoodId(1L);
        req.setQuantity(2);
        List<String> ingredients = new ArrayList<>();
        req.setIngredients(ingredients);
        String jwt = "jwt_token";

        User user = new User();
        when(userService.findUserByJwtToken(jwt)).thenReturn(user);

        Food food = new Food();
        food.setPrice(10L); // Set giá trị price để tránh lỗi NullPointerException
        when(foodService.findFoodById(req.getFoodId())).thenReturn(food);

        Cart cart = new Cart();
        when(cartRepository.findByCustomerId(user.getId())).thenReturn(cart);

        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> {
            CartItem newCartItem = invocation.getArgument(0);
            newCartItem.setId(1L); // Mô phỏng ID được tạo ra sau khi lưu vào cơ sở dữ liệu
            return newCartItem;
        });

        // Gọi phương thức để kiểm tra
        CartItem savedCartItem = cartService.addItemToCart(req, jwt);

        // Kiểm tra kết quả
        assertEquals(food, savedCartItem.getFood());
        assertEquals(cart, savedCartItem.getCart());
        assertEquals(req.getQuantity(), savedCartItem.getQuantity());
        assertEquals(ingredients, savedCartItem.getIngredients());
        assertEquals((long) req.getQuantity() * food.getPrice(), savedCartItem.getTotalPrice());
    }

    @Test
    public void testUpdateCartItemQuantity_Success() throws Exception {
        // Mock data
        Long cartItemId = 1L;
        int newQuantity = 3;
        Food mockFood = new Food();
        mockFood.setPrice(10L); // Assuming price per unit
        CartItem mockCartItem = new CartItem();
        mockCartItem.setId(cartItemId);
        mockCartItem.setFood(mockFood);

        // Mock behavior
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(mockCartItem));
        when(cartItemRepository.save(mockCartItem)).thenReturn(mockCartItem);

        // Call the method
        CartItem updatedItem = cartService.updateCartItemQuantity(cartItemId, newQuantity);

        // Assertions
        assertEquals(cartItemId, updatedItem.getId());
        assertEquals(newQuantity, updatedItem.getQuantity());
        assertEquals(newQuantity * mockFood.getPrice(), updatedItem.getTotalPrice());
        verify(cartItemRepository).save(mockCartItem); // Verify item is saved
    }

    @Test
    public void testFindCartById_Success() throws Exception {
         Cart mockCart;
        mockCart = new Cart();
        mockCart.setId(1L);
        // Mock behavior
        when(cartRepository.findById(1L)).thenReturn(Optional.of(mockCart));

        // Execute the method
        Cart foundCart = cartService.findCartById(1L);

        // Verify the result
        assertNotNull(foundCart);
        assertEquals(1L, foundCart.getId().longValue());
    }
    @Test
    public void testFindCartByUserId_Success() throws Exception {
        Cart mockCart;
        mockCart = new Cart();
        mockCart.setId(1L);
        // Mock behavior
        when(cartRepository.findByCustomerId(1L)).thenReturn(mockCart);

        // Execute the method
        Cart foundCart = cartService.findCartByUserId(1L);

        // Verify the result
        assertNotNull(foundCart);
        assertEquals(1L, foundCart.getId().longValue());
        // Add assertions for other properties if needed
    }

    @Test
    public void testClearCart_Success() throws Exception {
        Cart mockCart;

        mockCart = new Cart();
        mockCart.setId(1L);
        // Mock behavior
        when(cartRepository.findByCustomerId(1L)).thenReturn(mockCart);
        when(cartRepository.save(any())).thenReturn(mockCart);

        // Execute the method
        Cart clearedCart = cartService.clearCart(1L);

        // Verify the result
        assertNotNull(clearedCart);
        assertTrue(clearedCart.getItems().isEmpty());
        verify(cartRepository, times(1)).findByCustomerId(1L);
        verify(cartRepository, times(1)).save(mockCart);
    }


}
