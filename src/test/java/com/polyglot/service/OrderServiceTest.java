package com.polyglot.service;

import com.polyglot.dto.OrderReqRes;
import com.polyglot.entity.*;
import com.polyglot.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

public class OrderServiceTest {

    @Mock
    private UsersRepo usersRepo;

    @Mock
    private CartRepo cartRepo;

    @Mock
    private CartItemRepo cartItemRepo;

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private OrderService orderService;

    private OrderReqRes orderReqRes;
    private OurUsers user;
    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        user = new OurUsers();
        user.setId(1);
        user.setCartId(1);

        cart = new Cart();
        cart.setId(1);
        cart.setCartItems(new ArrayList<>());
        cart.setTotalPrice(100);

        cartItem = new CartItem();
        cartItem.setId(1);
        cartItem.setDishId(1);
        cartItem.setQuantity(2);
        cartItem.setTotalPrice(100); // total price for this item

        orderReqRes = new OrderReqRes();
        orderReqRes.setUserId(1);
    }

    // Test for the "checkoutCartAndCreateOrder" method

    @Test
    public void testCheckoutCartAndCreateOrder_UserNotFound() {
        // Mock user not found
        when(usersRepo.findById(orderReqRes.getUserId())).thenReturn(Optional.empty());

        OrderReqRes response = orderService.checkoutCartAndCreateOrder(orderReqRes);

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    public void testCheckoutCartAndCreateOrder_Success() {
        // Setup mock behaviors for a valid order creation
        when(usersRepo.findById(orderReqRes.getUserId())).thenReturn(Optional.of(user));
        when(cartRepo.findById(user.getCartId())).thenReturn(Optional.of(cart));
        when(cartItemRepo.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));
        when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1); // Mock the saved order having an ID
            return order;
        });

        // Mock the cart items in the user's cart
        cart.getCartItems().add(cartItem.getId());

        OrderReqRes response = orderService.checkoutCartAndCreateOrder(orderReqRes);

        assertEquals(200, response.getStatusCode());
        assertEquals(100, response.getOrderPrice());  // Total price from the cart
        assertEquals(1, response.getOrderItems().size());  // One item in the order
        assertEquals(1, response.getOrderId());  // Order ID after being saved
        assertEquals(0, user.getCartId());  // User's cart should be cleared after checkout
    }

    @Test
    public void testCheckoutCartAndCreateOrder_Exception() {
        // Simulate an exception during the process
        when(usersRepo.findById(orderReqRes.getUserId())).thenThrow(new RuntimeException("Unexpected error"));

        OrderReqRes response = orderService.checkoutCartAndCreateOrder(orderReqRes);

        assertEquals(500, response.getStatusCode());
        assertEquals("Unexpected error", response.getError());
    }

    // Test for the "getAllOrdersForUser" method

    @Test
    public void testGetAllOrdersForUser_UserNotFound() {
        when(usersRepo.findById(orderReqRes.getUserId())).thenReturn(Optional.empty());

        OrderReqRes response = orderService.getAllOrdersForUser(orderReqRes);

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    public void testGetAllOrdersForUser_Success() {
        // Setup mock behaviors for retrieving orders
        when(usersRepo.findById(orderReqRes.getUserId())).thenReturn(Optional.of(user));

        Order order1 = new Order();
        order1.setId(1);
        order1.setUserId(user.getId());
        order1.setStatus("PENDING");

        Order order2 = new Order();
        order2.setId(2);
        order2.setUserId(user.getId());
        order2.setStatus("PENDING");

        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepo.findAll()).thenReturn(orders);

        OrderReqRes response = orderService.getAllOrdersForUser(orderReqRes);

        assertEquals(200, response.getStatusCode());
        assertEquals(2, response.getOrders().size());  // Two orders for the user
    }

    @Test
    public void testGetAllOrdersForUser_Exception() {
        // Simulate an exception during the process
        when(usersRepo.findById(orderReqRes.getUserId())).thenThrow(new RuntimeException("Unexpected error"));

        OrderReqRes response = orderService.getAllOrdersForUser(orderReqRes);

        assertEquals(500, response.getStatusCode());
        assertEquals("Unexpected error", response.getError());
    }

    // Test for the "getOrderByOrderId" method

    @Test
    public void testGetOrderByOrderId_UserNotFound() {
        when(usersRepo.findById(orderReqRes.getUserId())).thenReturn(Optional.empty());

        OrderReqRes response = orderService.getOrderByOrderId(orderReqRes);

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    public void testGetOrderByOrderId_OrderNotFound() {
        // Mock user and order retrieval
        when(usersRepo.findById(orderReqRes.getUserId())).thenReturn(Optional.of(user));

        // Case where the order ID is not found
        when(orderRepo.findById(orderReqRes.getOrderId())).thenReturn(Optional.empty());

        OrderReqRes response = orderService.getOrderByOrderId(orderReqRes);

        assertEquals(404, response.getStatusCode());
        assertEquals("Valid OrderId not given in payload.", response.getMessage());
    }

    @Test
    public void testGetOrderByOrderId_Exception() {
        // Simulate an exception during the process
        when(usersRepo.findById(orderReqRes.getUserId())).thenThrow(new RuntimeException("Unexpected error"));

        OrderReqRes response = orderService.getOrderByOrderId(orderReqRes);

        assertEquals(500, response.getStatusCode());
        assertEquals("Unexpected error", response.getError());
    }
}
