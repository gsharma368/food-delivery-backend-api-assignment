package com.polyglot.service;

import com.polyglot.dto.ReqRes;
import com.polyglot.entity.Dish;
import com.polyglot.entity.OurUsers;
import com.polyglot.entity.Restaurant;
import com.polyglot.repository.DishRepo;
import com.polyglot.repository.RestaurantRepo;
import com.polyglot.repository.UsersRepo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UsersManagementServiceTest {
    @InjectMocks
    UsersManagementService usersManagementService = new UsersManagementService();

    @Mock
    private UsersRepo usersRepo; // Mock repository

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtilsHelper jwtUtilsHelper;

    @Mock
    private RestaurantRepo restaurantRepo;

    @Mock
    private DishRepo dishRepo;

    private Restaurant restaurant;
    private Dish dish1, dish2;

    private ReqRes reqRes = new ReqRes();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reqRes = new ReqRes();
        restaurant = new Restaurant();
        dish1 = new Dish();
        dish2 = new Dish();
        restaurant.setId(1);
        restaurant.setTitle("Test Restaurant");
        restaurant.setMenuDishes(Arrays.asList(1, 2));

        // Dishes
        dish1 = new Dish();
        dish1.setId(1);
        dish1.setName("Dish 1");
        dish1.setPrice(Integer.valueOf(String.valueOf(12.99)));

        dish2 = new Dish();
        dish2.setId(2);
        dish2.setName("Dish 2");
        dish2.setPrice(Integer.valueOf(String.valueOf(8.99)));
    }

    @Test
    public void registerUserSuccess() {
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

        OurUsers newUser = new OurUsers();
        newUser.setEmail(reqRes.getEmail());
        newUser.setCity(reqRes.getCity());
        newUser.setRole(reqRes.getRole());
        newUser.setName(reqRes.getName());
        newUser.setPassword(encodedPassword);
        newUser.setId(1); // Simulate a successful save (id > 0)

        when(usersRepo.save(Mockito.any(OurUsers.class))).thenReturn(newUser);

        // Act: Call the register method
        ReqRes result = usersManagementService.register(reqRes);

        // Assert: Verify that the status code is 200 and the message is correct
        assertEquals(200, result.getStatusCode());
        assertEquals("User Saved Successfully", result.getMessage());
        assertNotNull(result.getOurUsers()); // Ensure the user object is returned
        assertEquals(newUser.getEmail(), result.getOurUsers().getEmail());
    }

    @Test
    public void registerUserFailure() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword123");
        when(usersRepo.save(Mockito.any(OurUsers.class))).thenThrow(new RuntimeException("Database error"));

        // Act: Call the register method
        ReqRes result = usersManagementService.register(reqRes);

        // Assert: Verify that the status code is 500 and the error message is correct
        assertEquals(500, result.getStatusCode());
        assertTrue(result.getError().contains("Database error"));
    }

    @Test
    public void loginUserSuccess() {
        reqRes = new ReqRes();
        reqRes.setEmail("john.doe@example.com");
        reqRes.setPassword("securePassword123");
        OurUsers user = new OurUsers();
        user.setEmail(reqRes.getEmail());
        user.setRole("USER");

        when(usersRepo.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
        when(jwtUtilsHelper.generateJWTToken(user)).thenReturn("mockJWTToken");
        when(jwtUtilsHelper.generateJWTRefreshToken(Mockito.any(), eq(user))).thenReturn("mockRefreshToken");

        // Act: Call the login method
        ReqRes result = usersManagementService.login(reqRes);

        // Assert: Verify the response contains a token and the status code is 200
        assertEquals(200, result.getStatusCode());
        assertEquals("Successfully Logged In", result.getMessage());
        assertNotNull(result.getToken()); // Ensure JWT token is generated
        assertEquals("mockJWTToken", result.getToken());
    }

    @Test
    public void loginUserFailure() {
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act: Call the login method
        ReqRes result = usersManagementService.login(reqRes);

        // Assert: Verify that the status code is 500 and the message contains an error
        assertEquals(500, result.getStatusCode());
        assertTrue(result.getMessage().contains("Authentication failed"));
    }
    @Test
    public void getListOfUsersEmpty(){
        //Action
        ReqRes resp = usersManagementService.getAllUsers();
        //Verify
        System.out.println(resp);
        Assertions.assertThat(resp.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void getListOfUsersSuccess() {
        OurUsers user1 = new OurUsers();
        user1.setEmail("john.doe@example.com");
        user1.setRole("USER");

        OurUsers user2 = new OurUsers();
        user2.setEmail("jane.smith@example.com");
        user2.setRole("ADMIN");

        List<OurUsers> usersList = Arrays.asList(user1, user2);

        when(usersRepo.findAll()).thenReturn(usersList);

        // Act: Call the getAllUsers method
        ReqRes result = usersManagementService.getAllUsers();

        // Assert: Verify the response contains the users and the status code is 200
        assertEquals(200, result.getStatusCode());
        assertEquals("Successful", result.getMessage());
        assertEquals(2, result.getOurUsersList().size());
    }

    @Test
    public void getListOfUsersFailure() {
        when(usersRepo.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act: Call the getAllUsers method
        ReqRes result = usersManagementService.getAllUsers();

        // Assert: Verify the response has a 500 status code and the error message
        assertEquals(500, result.getStatusCode());
        assertTrue(result.getMessage().contains("Database error"));
    }

    @Test
    public void getUserById() {
        Integer userId = 1;
        OurUsers user = new OurUsers();
        user.setId(userId);
        user.setEmail("john.doe@example.com");
        user.setName("John Doe");

        // Mock the repository to return the user when findById is called
        when(usersRepo.findById(userId)).thenReturn(Optional.of(user));

        // Act: Call the getUsersById method
        ReqRes result = usersManagementService.getUsersById(userId);

        // Assert: Verify the status code, message, and returned user
        assertEquals(200, result.getStatusCode());
        assertEquals("Users with id '1' found successfully", result.getMessage());
        assertNotNull(result.getOurUsers()); // Ensure the user is returned
        assertEquals(user.getEmail(), result.getOurUsers().getEmail());
    }

    @Test
    public void updateUserSuccess() {
        Integer userId = 1;
        OurUsers existingUser = new OurUsers();
        existingUser.setId(userId);
        existingUser.setEmail("john.doe@example.com");
        existingUser.setName("John Doe");
        existingUser.setCity("New York");
        existingUser.setRole("USER");

        OurUsers updatedUser = new OurUsers();
        updatedUser.setEmail("john.doe_updated@example.com");
        updatedUser.setName("John Doe Updated");
        updatedUser.setCity("Los Angeles");
        updatedUser.setRole("ADMIN");
        updatedUser.setPassword("newpassword123");

        // Mock the repository to return the existing user
        when(usersRepo.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(updatedUser.getPassword())).thenReturn("encodedNewPassword");

        // Mock the repository to save the updated user
        when(usersRepo.save(existingUser)).thenReturn(existingUser);

        // Act: Call the updateUser method
        ReqRes result = usersManagementService.updateUser(userId, updatedUser);

        // Assert: Verify the response
        assertEquals(200, result.getStatusCode());
        assertEquals("User updated successfully", result.getMessage());
        assertEquals(updatedUser.getEmail(), result.getOurUsers().getEmail());
        assertEquals(updatedUser.getName(), result.getOurUsers().getName());
        assertEquals(updatedUser.getCity(), result.getOurUsers().getCity());
        assertEquals(updatedUser.getRole(), result.getOurUsers().getRole());
    }

    public void getMyInfoSuccess() {
        String email = "john.doe@example.com";
        OurUsers existingUser = new OurUsers();
        existingUser.setEmail(email);
        existingUser.setName("John Doe");
        existingUser.setCity("New York");
        existingUser.setRole("USER");

        // Mock the repository to return the existing user
        when(usersRepo.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // Act: Call the getMyInfo method
        ReqRes result = usersManagementService.getMyInfo(email);

        // Assert: Verify the response
        assertEquals(200, result.getStatusCode());
        assertEquals("successful", result.getMessage());
        assertEquals(existingUser.getEmail(), result.getOurUsers().getEmail());
        assertEquals(existingUser.getName(), result.getOurUsers().getName());
        assertEquals(existingUser.getCity(), result.getOurUsers().getCity());
        assertEquals(existingUser.getRole(), result.getOurUsers().getRole());
    }

    public void getMenuOfTheRestaurantIdSucess() {
        when(restaurantRepo.findById(1)).thenReturn(Optional.of(restaurant));
        when(dishRepo.findById(1)).thenReturn(Optional.of(dish1));
        when(dishRepo.findById(2)).thenReturn(Optional.of(dish2));

        // Act: Call the getMenuOfTheRestaurantId method
        List<Dish> menuDishes = usersManagementService.getMenuOfTheRestaurantId(1);

        // Assert: Verify the dishes are correctly returned
        assertEquals(2, menuDishes.size());
        assertTrue(menuDishes.contains(dish1));
        assertTrue(menuDishes.contains(dish2));
    }

    public void getMenuOfTheRestaurantIdFailure() {
        when(restaurantRepo.findById(99)).thenReturn(Optional.empty());

        // Act: Call the getMenuOfTheRestaurantId method
        List<Dish> menuDishes = usersManagementService.getMenuOfTheRestaurantId(99);

        // Assert: Verify an empty list is returned for non-existent restaurant
        assertEquals(0, menuDishes.size());
    }

    public void getMenuOfTheRestaurantId_dishesNotFound() {
        when(restaurantRepo.findById(1)).thenReturn(Optional.of(restaurant));
        when(dishRepo.findById(1)).thenReturn(Optional.of(dish1));
        when(dishRepo.findById(2)).thenReturn(Optional.empty());  // Dish 2 not found

        // Act: Call the getMenuOfTheRestaurantId method
        List<Dish> menuDishes = usersManagementService.getMenuOfTheRestaurantId(1);

        // Assert: Verify only the available dish is returned
        assertEquals(1, menuDishes.size());
        assertTrue(menuDishes.contains(dish1));
        assertFalse(menuDishes.contains(dish2));
    }
}
