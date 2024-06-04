package com.crud.api.entity;

import com.crud.api.enums.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "food_facts")
public class FoodFact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private Double calories;
    private Double value;
    private Double fat;
    private Double carbohydrate;
    private Double protein;
    private LocalDate date;
}
