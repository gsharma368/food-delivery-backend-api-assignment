package com.polyglot.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;

@Entity
@Table(name = "cart")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //private Map<Integer, Integer> dishQuantityMap;
    private Integer restaurantId;

    private Integer price;

}
