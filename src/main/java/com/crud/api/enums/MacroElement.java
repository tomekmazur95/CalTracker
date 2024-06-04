package com.crud.api.enums;

import lombok.Getter;

@Getter
public enum MacroElement {

    CARBOHYDRATE(4D, 0.5D), // 1g wegli dostarcza 4kcal energii
    FAT(9D, 0.2D),          // 1g tluszczu dostarcza 9kcal energii
    PROTEIN(4D, 0.3D);      // 1g bialka dostarcza 4kcal energii

    private final double caloriePerGram;
    private final double defaultPercentage;
    MacroElement(double caloriePerGram, double defaultPercentage) {
        this.caloriePerGram = caloriePerGram;
        this.defaultPercentage = defaultPercentage;
    }
}
