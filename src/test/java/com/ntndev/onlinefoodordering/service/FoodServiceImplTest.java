package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.dto.request.CreateFoodRequest;
import com.ntndev.onlinefoodordering.model.Category;
import com.ntndev.onlinefoodordering.model.Food;
import com.ntndev.onlinefoodordering.model.IngredientsItem;
import com.ntndev.onlinefoodordering.model.Restaurant;
import com.ntndev.onlinefoodordering.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FoodServiceImplTest {

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private Category category;

    @Mock
    private Restaurant restaurant;

    @InjectMocks
    private FoodServiceImpl foodService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testCreateFood() {
        // Mock data
        CreateFoodRequest req = new CreateFoodRequest();
        req.setDescription("Food Description");
        req.setImages(Arrays.asList("https://example.com/image1.jpg", "https://example.com/image2.jpg"));
        req.setName("Food Name");
        req.setPrice(100L);
        req.setSeasional(true);
        req.setVegetarian(true);

        // Mock behavior
        Food savedFood = new Food();
        savedFood.setId(1L);
        savedFood.setFoodCategory(category);
        savedFood.setRestaurant(restaurant);
        savedFood.setDescription(req.getDescription());
        savedFood.setImages(req.getImages());
        savedFood.setName(req.getName());
        savedFood.setPrice(req.getPrice());
        savedFood.setIngredients(req.getIngredients());
        savedFood.setSeasonal(req.isSeasional());
        savedFood.setVegetarian(req.isVegetarian());
        savedFood.setCreationDate(new Date());

        when(foodRepository.save(any())).thenReturn(savedFood);

        // Call the method
        Food createdFood = foodService.createFood(req, category, restaurant);

        // Verify the repository method was called with the correct Food object
        verify(foodRepository).save(any());

        // Verify that the returned Food object matches the savedFood object
        assertEquals(savedFood, createdFood);
    }

    @Test
    public void testDeleteFood_Success() throws Exception {
        // Mock data
        Long foodId = 1L;
        Food mockFood = new Food();
        mockFood.setId(foodId);

        // Mock behavior
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(mockFood));

        // Call the method
        foodService.deleteFood(foodId);

        // Assertions
        verify(foodRepository).findById(foodId);
        verify(foodRepository).save(mockFood);
        assertNull(mockFood.getRestaurant()); // Verify restaurant is set to null
    }



    @Test
    public void testGetRestaurantsFood_FilterByVegetarian() {
        // Mock data
        Long restaurantId = 1L;
        boolean isVegetarian = true;
        boolean isNonveg = false;
        boolean isSeasonal = false;
        String foodCategory = null;


        Restaurant rs = new Restaurant();
        Category category1 = new Category();
        List<String> images = new ArrayList<>();
        images.add("https://tse4.mm.bing.net/th?id=OIP.BXsxvnDMc7nQyCkgX5DgLwHaEK&pid=Api&P=0&h=180");
        images.add("https://tse2.mm.bing.net/th?id=OIP.uOrKohOOhaliitb2rcfUnwHaEK&pid=Api&P=0&h=180");


        // Sample food data
        List<Food> foods = Arrays.asList(
                new Food(1L, "Food 1", "Description 1", 100L, category1, images, true,rs, true, false, new ArrayList<>(), new Date()),
                new Food(2L, "Food 2", "Description 2", 150L, category1, images, false, rs, false, false, new ArrayList<>(), new Date())
                // Add more sample foods as needed
        );

        // Mock behavior of foodRepository
        when(foodRepository.findByRestaurantId(restaurantId)).thenReturn(foods);

        // Call the method to test
        List<Food> result = foodService.getRestaurantsFood(restaurantId, isVegetarian, isNonveg, isSeasonal, foodCategory);

        // Assert that the returned list of foods contains only vegetarian foods
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(Food::isVegetarian));
    }





    @Test
    public void testFindFoodById_ExistingFood() throws Exception {
        // Mock data
        Long foodId = 1L;
        Food mockFood = new Food();
        mockFood.setId(foodId);

        // Mock behavior
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(mockFood));

        // Call the method
        Food foundFood = foodService.findFoodById(foodId);

        // Assertions
        assertEquals(foodId, foundFood.getId());
    }
    @Test
    public void testFindFoodById_FoodDoesNotExist() {
        // Mock data
        Long foodId = 1L;

        // Mock behavior (food not found by ID)
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        // Call the method and expect an exception
        assertThrows(Exception.class, () -> {
            foodService.findFoodById(foodId);
        });
    }


    @Test
    public void testUpdateAvailibityStatus_FoodExists() throws Exception {
        // Mock data
        Long foodId = 1L;
        Food mockFood = new Food();
        mockFood.setId(foodId);
        mockFood.setAvailable(true);

        // Mock behavior (food found by ID)
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(mockFood));
        when(foodRepository.save(mockFood)).thenReturn(mockFood);

        // Call the method
        Food updatedFood = foodService.updateAvailibityStatus(foodId);

        // Verify the result
        assertNotNull(updatedFood);
        assertEquals(mockFood.isAvailable(), updatedFood.isAvailable());
    }




}
