package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.model.Category;
import com.ntndev.onlinefoodordering.model.Restaurant;
import com.ntndev.onlinefoodordering.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private RestaurantService restaurantService;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

//        categoryRepository = mock(CategoryRepository.class);
//        restaurantService = mock(RestaurantService.class);
//        categoryService = new CategoryService(categoryRepository, restaurantService);
    }

    @Test
    public void testCreateCategory() throws Exception {
        // Mock data
        Long userId = 1L;
        String categoryName = "Test Category";

        // Mock behavior
        Restaurant restaurant = new Restaurant(); // create a mock restaurant or use mockito
        when(restaurantService.getRestaurantByUserId(userId)).thenReturn(restaurant);

        // Call the method
        categoryService.createCategory(categoryName, userId);

        // Verify that save method of categoryRepository is called
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testFindCategoryByRestaurantId() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        List<Category> expectedCategories = new ArrayList<>(); // Create some mock categories or use Mockito

        // Mock behavior
        when(categoryRepository.findByRestaurantId(restaurantId)).thenReturn(expectedCategories);

        // Call the method
        List<Category> actualCategories = categoryService.findCategoryByRestaurantId(restaurantId);

        // Verify
        assertEquals(expectedCategories, actualCategories);
        verify(categoryRepository, times(1)).findByRestaurantId(restaurantId);
    }

    @Test
    public void testFindCategoryById_CategoryExists() throws Exception {
        // Mock data
        Long categoryId = 1L;
        Category expectedCategory = new Category(); // Create a mock category or use Mockito

        // Mock behavior
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));

        // Call the method
        Category actualCategory = categoryService.findCategoryById(categoryId);

        // Verify
        assertEquals(expectedCategory, actualCategory);
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testFindCategoryById_CategoryNotFound() {
        // Mock data
        Long categoryId = 1L;

        // Mock behavior
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Call the method and verify exception
        assertThrows(Exception.class, () -> categoryService.findCategoryById(categoryId));
        verify(categoryRepository, times(1)).findById(categoryId);
    }
}
