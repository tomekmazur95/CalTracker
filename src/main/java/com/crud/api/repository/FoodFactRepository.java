package com.crud.api.repository;

import com.crud.api.entity.FoodFact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodFactRepository extends JpaRepository<FoodFact, Long> {

}
