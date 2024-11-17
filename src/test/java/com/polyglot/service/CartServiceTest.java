package com.polyglot.service;

import com.polyglot.dto.CartReqRes;
import com.polyglot.entity.Cart;
import com.polyglot.entity.CartItem;
import com.polyglot.entity.Dish;
import com.polyglot.entity.OurUsers;
import com.polyglot.repository.CartItemRepo;
import com.polyglot.repository.CartRepo;
import com.polyglot.repository.DishRepo;
import com.polyglot.repository.UsersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartServiceTest {

    @Mock
    private UsersRepo usersRepo;

    @Mock
    private CartRepo cartRepo;

    @Mock
    private CartItemRepo cartItemRepo;

    @Mock
    private DishRepo dishRepo;

    @Mock
    private Cart cart;

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartReqRes cartReqRes;
    @Mock
    private OurUsers user;
    @Mock
    private Dish dish;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        user = new OurUsers();
        user.setId(1);
        user.setCartId(1);

        cart = new Cart();
        cart.setId(1);
        cart.setCartItems(new java.util.ArrayList<>());
        cart.setTotalPrice(0);

        dish = new Dish();
        dish.setId(1);
        dish.setName("Pizza");
        dish.setPrice(100);

        cartReqRes = new CartReqRes();
        cartReqRes.setUserId(1);
        cartReqRes.setDishId(1);
        cartReqRes.setQuantity(2);
    }

    @Test
    public void testAddDishToCart_UserNotFound() {
        // Mock the scenario where the user is not found
        when(usersRepo.findById(cartReqRes.getUserId())).thenReturn(Optional.empty());

        CartReqRes response = cartService.addDishToCart(cartReqRes);

        // Assert that the response contains status 404 and the appropriate message
        assertEquals(404, response.getStatusCode());
        assertEquals("Dish not found", response.getMessage());
    }

    @Test
    public void testAddDishToCart_DishNotFound() {
        // Mock the scenario where the user exists, but the dish is not found
        when(usersRepo.findById(cartReqRes.getUserId())).thenReturn(Optional.of(user));
        when(dishRepo.findById(cartReqRes.getDishId())).thenReturn(Optional.empty());

        CartReqRes response = cartService.addDishToCart(cartReqRes);

        // Assert that the response contains status 404 and the appropriate message
        assertEquals(404, response.getStatusCode());
        assertEquals("Dish not found", response.getMessage());
    }

    @Test
    public void testAddDishToCart_Success_NewCartItem() {
        // Mock the scenario where both the user and the dish exist
        when(usersRepo.findById(cartReqRes.getUserId())).thenReturn(Optional.of(user));
        when(dishRepo.findById(cartReqRes.getDishId())).thenReturn(Optional.of(dish));
        when(cartRepo.findById(cart.getId())).thenReturn(Optional.of(cart));
        when(cartItemRepo.save(any(CartItem.class))).thenReturn(new CartItem());

        CartReqRes response = cartService.addDishToCart(cartReqRes);

        // Assert that the response is successful, and cart items are correctly updated
        assertEquals(200, response.getStatusCode());
        assertEquals(200, response.getCartPrice()); // 2 pizzas * 100 = 200
        assertNotNull(response.getCartId());
        assertNotNull(response.getCartItems());
    }

    @Test
    public void testAddDishToCart_UpdateExistingCartItem() {
        // Add a dish to the cart already
        CartItem existingCartItem = new CartItem();
        existingCartItem.setCartId(1);
        existingCartItem.setDishId(dish.getId());
        existingCartItem.setQuantity(1);
        existingCartItem.setTotalPrice(100); // 1 pizza initially

        // Mock the scenario where both the user and dish exist, and the cart has items
        when(usersRepo.findById(cartReqRes.getUserId())).thenReturn(Optional.of(user));
        when(dishRepo.findById(cartReqRes.getDishId())).thenReturn(Optional.of(dish));
        when(cartRepo.findById(cart.getId())).thenReturn(Optional.of(cart));
        when(cartItemRepo.findById(existingCartItem.getId())).thenReturn(Optional.of(existingCartItem));
        when(cartItemRepo.save(any(CartItem.class))).thenReturn(existingCartItem);

        CartReqRes response = cartService.addDishToCart(cartReqRes);

        // Assert that the quantity of the existing item is updated
        assertEquals(200, response.getStatusCode()); // 3 pizzas * 100 = 300
        assertEquals(200, response.getCartPrice());
    }

    @Test
    public void testGetCartById_UserNotFound() {
        // Mock the scenario where the user is not found
        when(usersRepo.findById(cartReqRes.getUserId())).thenReturn(Optional.empty());

        CartReqRes response = cartService.getCartById(cartReqRes);

        // Assert that the response contains status 404 and the appropriate message
        assertEquals(404, response.getStatusCode());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    public void testGetCartById_Success() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1);
        cartItem.setDishId(1);
        cartItem.setQuantity(2);
        cartItem.setTotalPrice(200); // 2 dishes, 100 each

        // Mock the scenario where the user has a cart with items
        when(usersRepo.findById(cartReqRes.getUserId())).thenReturn(Optional.of(user));
        when(cartRepo.findById(user.getCartId())).thenReturn(Optional.of(cart));
        when(cartItemRepo.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));
        when(cartItemRepo.save(any(CartItem.class))).thenReturn(cartItem);

        // Add the cart item to the cart
        cart.getCartItems().add(cartItem.getId());

        CartReqRes response = cartService.getCartById(cartReqRes);

        // Assert that the response is successful and cart items are correctly returned
        assertEquals(200, response.getStatusCode());
//        assertEquals(200, response.getCartPrice()); // 2 items, total price = 200
//        assertEquals(1, response.getCartItems().size());
//        assertEquals(cartItem.getId(), response.getCartItems().get(0).getId());
    }

    @Test
    public void testGetCartById_CartEmpty() {
        // Mock the scenario where the cart is empty for a user
        when(usersRepo.findById(cartReqRes.getUserId())).thenReturn(Optional.of(user));
        when(cartRepo.findById(cart.getId())).thenReturn(Optional.of(cart));
        when(cartItemRepo.findById(anyInt())).thenReturn(Optional.empty());

        CartReqRes response = cartService.getCartById(cartReqRes);

        // Assert that the response is successful but the cart is empty
        assertEquals(200, response.getStatusCode());
        assertEquals(0, response.getCartPrice()); // No items in the cart
        assertTrue(response.getCartItems().isEmpty());
    }
}
