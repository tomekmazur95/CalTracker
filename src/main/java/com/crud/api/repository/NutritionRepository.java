package com.crud.api.repository;

import com.crud.api.entity.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutritionRepository extends JpaRepository<Nutrition, Long> {

    List<Nutrition> findAllByMeasurementId(Long id);
}
