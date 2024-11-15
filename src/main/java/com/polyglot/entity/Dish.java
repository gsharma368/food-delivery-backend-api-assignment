package com.polyglot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dishes")
@Data
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private long price;
    private String description;
    private String dishType; // 1 for veg, 2 for non veg
    private Integer restaurantId;
    private String cuisine;



}
