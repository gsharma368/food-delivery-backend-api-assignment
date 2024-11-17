package com.polyglot.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ElementCollection
    private List<Integer> orderItems = new ArrayList<>();

    private Integer totalPrice = 0;

    private boolean isPaymentDone = false;

    private Integer userId;
    private String status;

    public Order() {}

}
