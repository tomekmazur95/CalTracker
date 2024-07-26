package com.crud.api.util;

import static com.crud.api.enums.MacroElement.*;

public class NutritionUtils {

    private NutritionUtils() {
        // utility class
    }

    public static final double CARBS_DEFAULT_PERCENTAGE = CARBOHYDRATE.getDefaultPercentage();
    public static final double FAT_DEFAULT_PERCENTAGE = FAT.getDefaultPercentage();
    public static final double PROTEIN_DEFAULT_PERCENTAGE = PROTEIN.getDefaultPercentage();
    public static final double CARBS_PER_GRAM = CARBOHYDRATE.getCaloriePerGram();
    public static final double FAT_PER_GRAM = FAT.getCaloriePerGram();
    public static final double PROTEIN_PER_GRAM = PROTEIN.getCaloriePerGram();
}
