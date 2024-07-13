package com.crud.api.util;

import com.crud.api.enums.MacroElement;

public class NutritionUtils {

    private NutritionUtils() {
        // utility class
    }

    public static final double CARBS_DEFAULT_PERCENTAGE = MacroElement.CARBOHYDRATE.getDefaultPercentage();
    public static final double FAT_DEFAULT_PERCENTAGE = MacroElement.FAT.getDefaultPercentage();
    public static final double PROTEIN_DEFAULT_PERCENTAGE = MacroElement.PROTEIN.getDefaultPercentage();
    public static final double CARBS_PER_GRAM = MacroElement.CARBOHYDRATE.getCaloriePerGram();
    public static final double FAT_PER_GRAM = MacroElement.FAT.getCaloriePerGram();
    public static final double PROTEIN_PER_GRAM = MacroElement.PROTEIN.getCaloriePerGram();
}
