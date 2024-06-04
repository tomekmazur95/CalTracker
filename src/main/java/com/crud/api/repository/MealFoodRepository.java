package com.crud.api.repository;

import com.crud.api.entity.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealFoodRepository extends JpaRepository<MealFood, Long> {

    List<MealFood> findAllByMealId(Long mealId);

}
