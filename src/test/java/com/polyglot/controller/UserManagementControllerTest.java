package com.polyglot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polyglot.dto.DishReqRes;
import com.polyglot.dto.ReqRes;
import com.polyglot.dto.RestaurantReqRes;
import com.polyglot.entity.Dish;
import com.polyglot.entity.Restaurant;
import com.polyglot.entity.OurUsers;
import com.polyglot.repository.DishRepo;
import com.polyglot.repository.RestaurantRepo;
import com.polyglot.repository.UsersRepo;
import com.polyglot.service.UsersManagementService;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Spring automatically injects MockMvc

    @Mock
    private UsersRepo usersRepo;

    @Mock
    private RestaurantRepo restaurantRepo;

    @Mock
    private DishRepo dishRepo;

    @InjectMocks
    private UsersManagementService usersManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    private OurUsers mockUser;
    private Restaurant mockRestaurant;
    private Dish mockDish;

    @BeforeEach
    public void setup() {
        // Initialize mock entities
        mockUser = new OurUsers();
        mockUser.setId(1);
        mockUser.setEmail("testuser@example.com");
        mockUser.setPassword("password");
        mockUser.setName("Test User");
        mockUser.setRole("USER");

        mockRestaurant = new Restaurant();
        mockRestaurant.setId(1);
        mockRestaurant.setTitle("Test Restaurant");

        mockDish = new Dish();
        mockDish.setId(1);
        mockDish.setName("Test Dish");
        mockDish.setPrice(Integer.valueOf(String.valueOf(10.99)));
    }

    @Test
    public void testRegister() throws Exception {
        // Prepare test data
        ReqRes registerRequest = new ReqRes();
        registerRequest.setEmail("testuser@example.com");
        registerRequest.setPassword("password");
        registerRequest.setName("Test User");
        registerRequest.setRole("USER");

        // Mock service behavior
        when(usersRepo.save(any(OurUsers.class))).thenReturn(mockUser);

        // Perform POST request to register endpoint
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("User Saved Successfully"));
    }

    @Test
    public void testLogin() throws Exception {
        // Prepare test data
        ReqRes loginRequest = new ReqRes();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("password");

        // Mock service behavior
        when(usersRepo.findByEmail(anyString())).thenReturn(java.util.Optional.of(mockUser));

        // Perform POST request to login endpoint
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Successfully Logged In"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        // Mock service behavior
        when(usersRepo.findAll()).thenReturn(java.util.Collections.singletonList(mockUser));

        // Perform GET request to get all users endpoint
        mockMvc.perform(get("/admin/get-all-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Successful"))
                .andExpect(jsonPath("$.ourUsersList[0].id").value(mockUser.getId()))
                .andExpect(jsonPath("$.ourUsersList[0].email").value(mockUser.getEmail()));
    }

    @Test
    public void testGetUserById() throws Exception {
        // Mock service behavior
        when(usersRepo.findById(anyInt())).thenReturn(java.util.Optional.of(mockUser));

        // Perform GET request to get user by ID endpoint
        mockMvc.perform(get("/admin/get-users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Users with id '1' found successfully"))
                .andExpect(jsonPath("$.ourUsers.id").value(mockUser.getId()));
    }

    @Test
    public void testAddRestaurant() throws Exception {
        // Prepare test data
        RestaurantReqRes restaurantReqRes = new RestaurantReqRes();
        restaurantReqRes.setTitle("Test Restaurant");
        restaurantReqRes.setDescription("A test restaurant");
        restaurantReqRes.setContent("Content of test restaurant");

        // Mock service behavior
        when(restaurantRepo.save(any(Restaurant.class))).thenReturn(mockRestaurant);

        // Perform POST request to add restaurant endpoint
        mockMvc.perform(post("/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantReqRes)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Restaurant Saved Successfully"));
    }

    @Test
    public void testAddDishToRestaurant() throws Exception {
        // Prepare test data
        DishReqRes dishReqRes = new DishReqRes();
        dishReqRes.setDishName("Test Dish");
        dishReqRes.setDishPrice(Integer.valueOf(String.valueOf(12.99)));
        dishReqRes.setDishDescription("A test dish description");
        dishReqRes.setDishType("Appetizer");
        dishReqRes.setDishCuisine("Italian");
        dishReqRes.setRestaurantId(1);

        // Mock service behavior
        when(dishRepo.save(any(Dish.class))).thenReturn(mockDish);

        // Perform POST request to add dish to restaurant endpoint
        mockMvc.perform(post("/add-dish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishReqRes)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Dish Added Successfully to the Restaurant Content"));
    }

    @Test
    public void testGetMenuOfRestaurant() throws Exception {
        // Prepare test data
        Dish dish1 = new Dish();
        dish1.setId(1);
        dish1.setName("Test Dish 1");

        Dish dish2 = new Dish();
        dish2.setId(2);
        dish2.setName("Test Dish 2");

        // Mock service behavior
        when(restaurantRepo.findById(1)).thenReturn(java.util.Optional.of(mockRestaurant));
        when(dishRepo.findById(1)).thenReturn(java.util.Optional.of(dish1));
        when(dishRepo.findById(2)).thenReturn(java.util.Optional.of(dish2));

        // Perform GET request to get restaurant's menu
        mockMvc.perform(get("/course/1/get-menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(dish1.getName()))
                .andExpect(jsonPath("$[1].name").value(dish2.getName()));
    }
}

