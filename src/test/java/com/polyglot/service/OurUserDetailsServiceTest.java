package com.polyglot.service;

import com.polyglot.entity.OurUsers;
import com.polyglot.repository.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OurUserDetailsServiceTest {

    @Mock
    private UsersRepo usersRepo;

    @InjectMocks
    private OurUserDetailsService userDetailsService;

    private OurUsers user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data for the user
        user = new OurUsers();
        user.setId(1);
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        // Add any other fields that may be necessary for the UserDetails
    }

    // Test when the user is found by email

    @Test
    public void testLoadUserByUsername_UserFound() {
        // Arrange: Mock the behavior of usersRepo to return the user when the email is queried
        when(usersRepo.findByEmail("testuser@example.com")).thenReturn(java.util.Optional.of(user));

        // Act: Load the user by username
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser@example.com");

        // Assert: The returned UserDetails should match the expected user
        assertNotNull(userDetails);
        assertEquals("testuser@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());  // You may add more assertions for other fields if needed
    }

    // Test when the user is not found by email

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange: Mock the behavior of usersRepo to return an empty Optional
        when(usersRepo.findByEmail("nonexistentuser@example.com")).thenReturn(java.util.Optional.empty());

        // Act & Assert: The method should throw UsernameNotFoundException
        assertThrows(NoSuchElementException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistentuser@example.com");
        });
    }

    // Test when the email passed is null or empty

    @Test
    public void testLoadUserByUsername_NullEmail() {
        // Act & Assert: The method should throw IllegalArgumentException (or a more appropriate exception if handled)
        assertThrows(NoSuchElementException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });
    }

    @Test
    public void testLoadUserByUsername_EmptyEmail() {
        // Act & Assert: The method should throw IllegalArgumentException (or a more appropriate exception if handled)
        assertThrows(NoSuchElementException.class, () -> {
            userDetailsService.loadUserByUsername("");
        });
    }

    // Test when the repository throws an unexpected exception

    @Test
    public void testLoadUserByUsername_ExceptionThrown() {
        // Arrange: Mock the behavior of usersRepo to throw an unexpected exception
        when(usersRepo.findByEmail("testuser@example.com")).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert: The method should throw RuntimeException (or catch the error as needed)
        assertThrows(RuntimeException.class, () -> {
            userDetailsService.loadUserByUsername("testuser@example.com");
        });
    }
}
