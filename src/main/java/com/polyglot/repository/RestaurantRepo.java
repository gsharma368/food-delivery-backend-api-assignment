package com.polyglot.repository;

import com.polyglot.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepo extends JpaRepository<Restaurant, Integer> {

    List<Restaurant> findAll();
}
