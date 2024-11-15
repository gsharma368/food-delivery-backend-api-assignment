package com.polyglot.repository;

import com.polyglot.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepo extends JpaRepository<Dish, Integer> {

    List<Dish> findAll();
}
