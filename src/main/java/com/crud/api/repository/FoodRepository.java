package com.crud.api.repository;

import com.crud.api.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    @Query("SELECT f FROM Food f JOIN FETCH f.foodFact WHERE f.user.id = :userId")
    List<Food> findAllByUserId(Long userId);
}
