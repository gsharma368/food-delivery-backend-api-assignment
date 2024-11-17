package com.polyglot.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "cartItem")
@Data
public class CartItem {
    // Getters and Setters
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer cartId;

    private Integer dishId;

    private String name;
    private Integer price;
    private String description;
    private String dishType; // 1 for veg, 2 for non veg
    private String cuisine;


    private Integer quantity;

    private Integer totalPrice;

    public CartItem() {}


}

