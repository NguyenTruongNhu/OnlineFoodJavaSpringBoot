package com.ntndev.onlinefoodordering.service;

import com.ntndev.onlinefoodordering.config.JwtProvider;
import com.ntndev.onlinefoodordering.dto.RestaurantDto;
import com.ntndev.onlinefoodordering.dto.request.CreateRestaurantRequest;
import com.ntndev.onlinefoodordering.model.Address;
import com.ntndev.onlinefoodordering.model.ContactInformation;
import com.ntndev.onlinefoodordering.model.Restaurant;
import com.ntndev.onlinefoodordering.model.User;
import com.ntndev.onlinefoodordering.repository.AddressRepository;
import com.ntndev.onlinefoodordering.repository.RestaurantRepository;
import com.ntndev.onlinefoodordering.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant;
    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    RestaurantServiceImpl restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateRestaurant() {
        // Mocking input data
        CreateRestaurantRequest req = new CreateRestaurantRequest();
        ContactInformation ci = new ContactInformation();
        ci.setEmail("nhu81632@gmail.com");
        ci.setInstagram("instagram.com");
        ci.setMobile("09456454521");
        ci.setTwitter("twitter.com");

        List<String> images = new ArrayList<>();
        images.add("https://tse4.mm.bing.net/th?id=OIP.BXsxvnDMc7nQyCkgX5DgLwHaEK&pid=Api&P=0&h=180");
        images.add("https://tse2.mm.bing.net/th?id=OIP.uOrKohOOhaliitb2rcfUnwHaEK&pid=Api&P=0&h=180");


        Address address = new Address();
        req.setAddress(address);
        req.setContactInformation(ci);
        req.setCuisineType("cuisine type");
        req.setDescription("description");
        req.setImages(images);
        req.setName("restaurant name");
        req.setOpeningHours("opening hours");


        User user = new User();

        // Mocking the behavior of addressRepository.save
        Address savedAddress = new Address();
        when(addressRepository.save(any())).thenReturn(savedAddress);

        // Mocking the behavior of restaurantRepository.save
        Restaurant savedRestaurant = new Restaurant();
        when(restaurantRepository.save(any())).thenReturn(savedRestaurant);

        // Calling the method under test
        Restaurant createdRestaurant = restaurantService.createRestaurant(req, user);


        // Verifying that the returned restaurant matches the expected restaurant
        assertEquals(savedRestaurant, createdRestaurant);
    }

    @Test
    public void testUpdateRestaurant() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        CreateRestaurantRequest updatedRestaurantRequest = new CreateRestaurantRequest();
        updatedRestaurantRequest.setName("New Name");
        updatedRestaurantRequest.setDescription("New Description");
        updatedRestaurantRequest.setCuisineType("New Cuisine");

        Restaurant existingRestaurant = new Restaurant();
        existingRestaurant.setId(restaurantId);
        existingRestaurant.setName("Old Name");
        existingRestaurant.setDescription("Old Description");
        existingRestaurant.setCuisineType("Old Cuisine");

        // Mock behavior
        when(restaurantRepository.findById(restaurantId)).thenReturn(java.util.Optional.of(existingRestaurant));
        when(restaurantRepository.save(existingRestaurant)).thenReturn(existingRestaurant);

        // Call the method
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(restaurantId, updatedRestaurantRequest);

        // Verify that repository method was called
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(existingRestaurant);

        // Verify the result
        assertEquals(updatedRestaurantRequest.getName(), updatedRestaurant.getName());
        assertEquals(updatedRestaurantRequest.getDescription(), updatedRestaurant.getDescription());
        assertEquals(updatedRestaurantRequest.getCuisineType(), updatedRestaurant.getCuisineType());
    }


    @Test
    public void testDeleteRestaurant() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        Restaurant existingRestaurant = new Restaurant();
        existingRestaurant.setId(restaurantId);

        // Mock behavior
        when(restaurantRepository.findById(restaurantId)).thenReturn(java.util.Optional.of(existingRestaurant));

        // Call the method
        restaurantService.deleteRestaurant(restaurantId);

        // Verify that repository method was called
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).delete(existingRestaurant);
    }

    @Test
    public void testGetAllRestaurant() {
        // Mock data
        List<Restaurant> restaurants = Arrays.asList(new Restaurant(), new Restaurant());

        // Mock behavior
        when(restaurantRepository.findAll()).thenReturn(restaurants);

        // Call the method
        List<Restaurant> result = restaurantService.getAllRestaurant();

        // Verify that repository method was called
        verify(restaurantRepository).findAll();

        // Verify the result
        assertEquals(restaurants.size(), result.size());
    }

    @Test
    public void testSearchRestaurant() {
        // Mock data
        String keyword = "keyword";
        List<Restaurant> restaurants = Arrays.asList(new Restaurant(), new Restaurant());

        // Mock behavior
        when(restaurantRepository.findBySearchQuery(keyword)).thenReturn(restaurants);

        // Call the method
        List<Restaurant> result = restaurantService.searchRestaurant(keyword);

        // Verify that repository method was called
        verify(restaurantRepository).findBySearchQuery(keyword);

        // Verify the result
        assertEquals(restaurants.size(), result.size());
    }

    @Test
    public void testFindRestaurantById_RestaurantNotFound() {
        // Mock data
        Long id = 1L;
        Optional<Restaurant> emptyOptional = Optional.empty();

        // Mock behavior
        when(restaurantRepository.findById(id)).thenReturn(emptyOptional);

        // Call the method and assert
        Exception exception = assertThrows(Exception.class, () -> {
            restaurantService.findRestaurantById(id);
        });

        // Verify that repository method was called
        verify(restaurantRepository).findById(id);


        assertEquals("Restaurant not found with id" + id, exception.getMessage());

    }
    @Test
    public void testFindRestaurantById_RestaurantFound() throws Exception {
        // Mock data
        Long id = 1L;
        Restaurant restaurant = new Restaurant();

        // Mock behavior
        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));

        // Call the method
        Restaurant foundRestaurant = restaurantService.findRestaurantById(id);

        // Verify that repository method was called
        verify(restaurantRepository).findById(id);

        // Verify the result
        assertNotNull(foundRestaurant);
    }

    @Test
    public void testGetRestaurantByUserId_RestaurantFound() throws Exception {
        // Mock data
        Long userId = 1L;
        Restaurant restaurant = new Restaurant();

        // Mock behavior
        when(restaurantRepository.findByOwnerId(userId)).thenReturn(restaurant);

        // Call the method
        Restaurant foundRestaurant = restaurantService.getRestaurantByUserId(userId);

        // Verify that repository method was called
        verify(restaurantRepository).findByOwnerId(userId);

        // Verify the result
        assertNotNull(foundRestaurant);
    }
    @Test
    public void testGetRestaurantByUserId_RestaurantNotFound() {
        // Mock data
        Long userId = 1L;

        // Mock behavior
        when(restaurantRepository.findByOwnerId(userId)).thenReturn(null);

        // Call the method and assert
        Exception exception = assertThrows(Exception.class, () -> {
            restaurantService.getRestaurantByUserId(userId);
        });

        // Verify that repository method was called
        verify(restaurantRepository).findByOwnerId(userId);

        // Verify the exception message
        assertTrue(exception.getMessage().contains("Restaurant not found with owner id" + userId));
    }

    @Test
    public void testAddToFavorites_AddFavorite() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        User user = new User();
        List<RestaurantDto> favorites = new ArrayList<>();
        List<String> images = new ArrayList<>();
        images.add("https://tse4.mm.bing.net/th?id=OIP.BXsxvnDMc7nQyCkgX5DgLwHaEK&pid=Api&P=0&h=180");
        images.add("https://tse2.mm.bing.net/th?id=OIP.uOrKohOOhaliitb2rcfUnwHaEK&pid=Api&P=0&h=180");


        user.setFavorites(favorites);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Restaurant Name");
        restaurant.setDescription("Restaurant Description");
        restaurant.setImages(images);

        // Mock behavior
        when(restaurantRepository.findById(restaurantId)).thenReturn(java.util.Optional.of(restaurant));
        when(userRepository.save(user)).thenReturn(user);

        // Call the method
        RestaurantDto dto = restaurantService.addToFavorites(restaurantId, user);

        // Verify that repository method was called
        verify(restaurantRepository).findById(restaurantId);
        verify(userRepository).save(user);

        // Verify the result
        assertTrue(user.getFavorites().contains(dto));
    }

    @Test
    public void testAddToFavorites_RemoveFavorite() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        User user = new User();
        List<RestaurantDto> favorites = new ArrayList<>();
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurantId);
        favorites.add(dto);
        user.setFavorites(favorites);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        // Mock behavior
        when(restaurantRepository.findById(restaurantId)).thenReturn(java.util.Optional.of(restaurant));
        when(userRepository.save(user)).thenReturn(user);

        // Call the method
        restaurantService.addToFavorites(restaurantId, user);

        // Verify that repository method was called
        verify(restaurantRepository).findById(restaurantId);
        verify(userRepository).save(user);

        // Verify the result
        assertFalse(user.getFavorites().contains(dto));
    }

    @Test
    public void testUpdateRestaurantStatus() throws Exception {
        // Mock data
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        boolean initialStatus = restaurant.isOpen();

        // Mock behavior
        when(restaurantRepository.findById(restaurantId)).thenReturn(java.util.Optional.of(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        // Call the method
        Restaurant updatedRestaurant = restaurantService.updateRestaurantStatus(restaurantId);

        // Verify that repository method was called
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(restaurant);

        // Verify the result
        assertEquals(!initialStatus, updatedRestaurant.isOpen());
    }

}





