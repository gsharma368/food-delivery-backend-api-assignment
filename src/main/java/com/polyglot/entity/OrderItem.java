package com.polyglot.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orderItem")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer dishId;

    private Integer quantity;
    private Integer totalPrice;

    public OrderItem() {}

    // Constructor to create an OrderItem from a CartItem
    public OrderItem(CartItem cartItem) {
        this.dishId = cartItem.getDishId();
        this.quantity = cartItem.getQuantity();
        this.totalPrice = cartItem.getTotalPrice();
    }

}

