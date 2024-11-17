package com.polyglot.controller;

import com.polyglot.dto.CartReqRes;
import com.polyglot.entity.*;
import com.polyglot.repository.CartRepo;
import com.polyglot.repository.DishRepo;
import com.polyglot.repository.RestaurantRepo;
import com.polyglot.repository.UsersRepo;
import com.polyglot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userCart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private RestaurantRepo restaurantRepo;
    @Autowired
    private DishRepo dishRepo;
    @Autowired
    private CartRepo cartRepo;

    // Create a new cart
    @PostMapping
    public ResponseEntity<CartReqRes> createDishToCart(@RequestBody CartReqRes cartReqRes) {
        CartReqRes savedCart = cartService.addDishToCart(cartReqRes);
        return new ResponseEntity<>(savedCart, HttpStatus.CREATED);
    }

    // Get a specific cart by ID
    @GetMapping("/{cartId}")
    public ResponseEntity<CartReqRes> getCart(@PathVariable Integer cartId) {
        CartReqRes cart = cartService.getCartById(cartId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

//    // Get all carts (optional)
//    @GetMapping
//    public ResponseEntity<List<Cart>> getAllCarts() {
//        List<Cart> carts = cartService.getAllCarts();
//        return new ResponseEntity<>(carts, HttpStatus.OK);
//    }
//
//    // Add an item to the cart
//    @PostMapping("/{cartId}/items")
//    public ResponseEntity<Cart> addItemToCart(@PathVariable Long cartId, @RequestBody CartItem cartItem) {
//        Cart updatedCart = cartService.addItemToCart(cartId, cartItem);
//        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
//    }
//
//    // Remove an item from the cart
//    @DeleteMapping("/{cartId}/items/{itemId}")
//    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
//        Cart updatedCart = cartService.removeItemFromCart(cartId, itemId);
//        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
//    }
//
//    // Update the quantity of an item in the cart
//    @PutMapping("/{cartId}/items/{itemId}")
//    public ResponseEntity<Cart> updateItemQuantity(@PathVariable Long cartId, @PathVariable Long itemId, @RequestParam int quantity) {
//        Cart updatedCart = cartService.updateItemQuantity(cartId, itemId, quantity);
//        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
//    }
//
//    // Delete a cart
//    @DeleteMapping("/{cartId}")
//    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
//        cartService.deleteCart(cartId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}

