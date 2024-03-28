package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.model.IngredientCategory;
import com.ntndev.onlinefoodordering.model.IngredientsItem;
import com.ntndev.onlinefoodordering.model.Restaurant;
import com.ntndev.onlinefoodordering.repository.IngredientCategoryRepository;
import com.ntndev.onlinefoodordering.repository.IngredientItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceImplTest {
    @Mock
    private RestaurantService restaurantService;
    @Mock
    private IngredientItemRepository ingredientItemRepository;

    @Mock
    private IngredientCategoryRepository ingredientCategoryRepository;

    @InjectMocks
    private IngredientServiceImpl ingredientCategoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateIngredientCategory() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        String categoryName = "Vegetables";
        Restaurant restaurant = new Restaurant();
        when(restaurantService.findRestaurantById(restaurantId)).thenReturn(restaurant);

        // Create the ingredient category
        IngredientCategory category = new IngredientCategory();
        category.setRestaurant(restaurant);
        category.setName(categoryName);
        when(ingredientCategoryRepository.save(any(IngredientCategory.class))).thenReturn(category);

        // Call the method
        IngredientCategory createdCategory = ingredientCategoryService.createIngredientCategory(categoryName, restaurantId);

        // Verify that service method was called with correct parameter
        verify(restaurantService).findRestaurantById(restaurantId);

        // Verify that repository method was called with correct parameter
        verify(ingredientCategoryRepository).save(any(IngredientCategory.class));

        // Verify the result
        assertNotNull(createdCategory);
        assertEquals(categoryName, createdCategory.getName());
        assertEquals(restaurant, createdCategory.getRestaurant());
    }
    @Test
    public void testFindIngredientCategoryById_ExistingId() throws Exception {
        // Mock data
        Long categoryId = 1L;
        IngredientCategory expectedCategory = new IngredientCategory();
        when(ingredientCategoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));

        // Call the method
        IngredientCategory actualCategory = ingredientCategoryService.findIngredientCategoryById(categoryId);

        // Verify that repository method was called with correct parameter
        verify(ingredientCategoryRepository).findById(categoryId);

        // Verify the result
        assertNotNull(actualCategory);
        assertEquals(expectedCategory, actualCategory);
    }
    @Test
    public void testFindIngredientCategoryById_NonExistingId() {
        // Mock data
        Long categoryId = 1L;
        when(ingredientCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Verify that the method throws an exception for a non-existing ID
        assertThrows(Exception.class, () -> ingredientCategoryService.findIngredientCategoryById(categoryId));

        // Verify that repository method was called with correct parameter
        verify(ingredientCategoryRepository).findById(categoryId);
    }
    @Test
    public void testFindIngredientCategoryByRestaurantId() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        when(restaurantService.findRestaurantById(restaurantId)).thenReturn(restaurant);

        List<IngredientCategory> expectedCategories = new ArrayList<>();
        expectedCategories.add(new IngredientCategory());
        expectedCategories.add(new IngredientCategory());
        when(ingredientCategoryRepository.findByRestaurantId(restaurantId)).thenReturn(expectedCategories);

        // Call the method
        List<IngredientCategory> actualCategories = ingredientCategoryService.findIngredientCategoryByRestaurantId(restaurantId);

        // Verify that restaurantService method was called with correct parameter
        verify(restaurantService).findRestaurantById(restaurantId);

        // Verify that repository method was called with correct parameter
        verify(ingredientCategoryRepository).findByRestaurantId(restaurantId);

        // Verify the result
        assertNotNull(actualCategories);
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertEquals(expectedCategories, actualCategories);
    }

    @Test
    public void testCreateIngredientItem_RestaurantNotFound() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        Long categoryId = 1L;
        String ingredientName = "Salt";

        // Mock service method calls
        when(restaurantService.findRestaurantById(restaurantId)).thenReturn(null);

        // Verify that an exception is thrown when the restaurant is not found
        Exception exception = assertThrows(Exception.class, () -> {
            ingredientCategoryService.createIngredientItem(restaurantId, ingredientName, categoryId);
        });
        assertEquals("Restaurant not found with ID: " + restaurantId, exception.getMessage());

        // Verify that repository method was not called
        verify(ingredientItemRepository, never()).save(any(IngredientsItem.class));
    }

    @Test
    public void testCreateIngredientItem_Success() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        String ingredientName = "Pizza Dough";
        Long categoryId = 2L;
        Restaurant mockRestaurant = new Restaurant();
        IngredientCategory mockCategory = new IngredientCategory();

        // ArgumentCaptor to capture saved item
        ArgumentCaptor<IngredientsItem> argumentCaptor = ArgumentCaptor.forClass(IngredientsItem.class);

        // Mock behavior
        when(restaurantService.findRestaurantById(restaurantId)).thenReturn(mockRestaurant);
        when(ingredientCategoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));

        // Call the method
        IngredientsItem ingredient = ingredientCategoryService.createIngredientItem(restaurantId, ingredientName, categoryId);

        // Assertions
        verify(restaurantService).findRestaurantById(restaurantId);
        verify(ingredientCategoryRepository).findById(categoryId);
        verify(ingredientItemRepository).save(argumentCaptor.capture());

        IngredientsItem savedItem = argumentCaptor.getValue();

        assertEquals(ingredientName, savedItem.getName());
        assertEquals(mockRestaurant, savedItem.getRestaurant());
        assertEquals(mockCategory, savedItem.getCategory());
    }


    @Test
    public void testFindRestaurantsIngredients() {
        // Mock data
        Long restaurantId = 1L;
        IngredientsItem item1 = new IngredientsItem();
        IngredientsItem item2 = new IngredientsItem();
        List<IngredientsItem> expectedIngredients = Arrays.asList(item1, item2);

        // Mock repository method call
        when(ingredientItemRepository.findByRestaurantId(restaurantId)).thenReturn(expectedIngredients);

        // Call the service method
        List<IngredientsItem> actualIngredients = ingredientCategoryService.findRestaurantsIngredients(restaurantId);

        // Verify that the repository method was called with the correct restaurant ID
        verify(ingredientItemRepository).findByRestaurantId(restaurantId);

        // Verify that the returned list of ingredients matches the expected list
        assertEquals(expectedIngredients, actualIngredients);
    }


    @Test
    public void testUpdateStock() throws Exception {
        // Mock data
        Long itemId = 1L;
        IngredientsItem item = new IngredientsItem();
        item.setId(itemId);
        item.setInStoke(true);

        // Mock repository method call
        when(ingredientItemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(ingredientItemRepository.save(item)).thenReturn(item);

        // Call the service method
        IngredientsItem updatedItem = ingredientCategoryService.updateStock(itemId);

        // Verify that the repository method was called with the correct ID
        verify(ingredientItemRepository).findById(itemId);

        // Verify that the item's stock status was toggled
        assertFalse(updatedItem.isInStoke());

        // Verify that the repository method was called to save the updated item
        verify(ingredientItemRepository).save(item);
    }

    @Test
    public void testUpdateStock_IngredientNotFound() throws Exception {
        // Mock data
        Long id = 1L;

        // Mock behavior (ingredient not found)
        when(ingredientItemRepository.findById(id)).thenReturn(Optional.empty());

        // Call the method (expecting exception)
        Exception exception = assertThrows(Exception.class, () -> {
            ingredientCategoryService.updateStock(id);
        });

        // Verify the exception message
        assertEquals("ingredient not found", exception.getMessage());
    }



}
