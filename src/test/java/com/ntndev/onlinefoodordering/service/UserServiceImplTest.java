package com.ntndev.onlinefoodordering.service;


import com.ntndev.onlinefoodordering.config.JwtProvider;
import com.ntndev.onlinefoodordering.model.USER_ROLE;
import com.ntndev.onlinefoodordering.model.User;
import com.ntndev.onlinefoodordering.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    private JwtProvider jwtProvider;


    private User user = new User();

    @BeforeEach
    void setUp() {

//        user = new User("TruongNhu","nhu81632@gmail.com","123456", USER_ROLE.ROLE_RESTAURANT_OWNER);

        MockitoAnnotations.initMocks(this);

    }

    @Test
    void testFindByEmail_Found() throws Exception {
        String email = "nhu81632@gmail.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        // When
        User foundUser = userService.findUserByEmail(email);

        // Then
        assertEquals(user, foundUser);
    }

    @Test
    void testFindByEmail_Not_Found() throws Exception {
        String email = "example@gmail.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
                    userService.findUserByEmail(email);
                });
            assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testFindUserByJwtToken() throws Exception {
        // Mocking the behavior of jwtProvider.getEmailFromJwtToken
        String jwt = "sampleJwtToken";
        String email = "test@example.com";
        when(jwtProvider.getEmailFromJwtToken(jwt)).thenReturn(email);

        // Mocking the behavior of userRepository.findUserByEmail
        User expectedUser = new User();
        expectedUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        // Calling the method under test
        User actualUser = userService.findUserByJwtToken(jwt);

        // Verifying the result
        assertEquals(expectedUser, actualUser);
    }



}
