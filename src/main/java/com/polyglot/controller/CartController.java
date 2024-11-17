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
    @GetMapping("")
    public ResponseEntity<CartReqRes> getCart(@RequestBody CartReqRes cartReqRes) {
        CartReqRes cart = cartService.getCartById(cartReqRes);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

}

