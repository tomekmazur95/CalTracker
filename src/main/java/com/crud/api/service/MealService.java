package com.crud.api.service;

import com.crud.api.dto.RequestMealDTO;
import com.crud.api.dto.ResponseMealDTO;
import com.crud.api.entity.Food;
import com.crud.api.entity.Meal;
import com.crud.api.entity.MealFood;
import com.crud.api.entity.User;
import com.crud.api.error.FoodNotFoundException;
import com.crud.api.error.UserNotFoundException;
import com.crud.api.mapper.MealFoodMapper;
import com.crud.api.mapper.RequestMealMapper;
import com.crud.api.mapper.ResponseMealMapper;
import com.crud.api.repository.FoodRepository;
import com.crud.api.repository.MealFoodRepository;
import com.crud.api.repository.MealRepository;
import com.crud.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MealService {
    private static final String USER_NOT_FOUND = "User with id: %s not found";
    private static final String FOOD_NOT_FOUND = "Food with id: %s not found";

    private final FoodRepository foodRepository;
    private final MealRepository mealRepository;
    private final MealFoodRepository mealFoodRepository;
    private final UserRepository userRepository;
    private final RequestMealMapper requestMealMapper;
    private final MealFoodMapper mealFoodMapper;
    private final ResponseMealMapper responseMealMapper;

    @Transactional
    public ResponseMealDTO createMeal(RequestMealDTO dto, Long userId) {
        if (dto.getFoodList() == null || dto.getFoodList().isEmpty()) {
            throw new IllegalArgumentException("Food list must have at least one element");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));

        Map<Food, Double> mealFoodMap = new HashMap<>();
        dto.getFoodList().forEach(e -> mealFoodMap.put(fetchFood(e.getFoodId()), e.getQuantity()));

        Meal domain = requestMealMapper.toDomain(dto, user);
        mealRepository.save(domain);

        mealFoodMap.entrySet()
                .stream()
                .map(mealFoodMapper::toDomain)
                .forEach(e -> {
                    e.setMeal(domain);
                    mealFoodRepository.save(e);
                });
        return responseMealMapper.fromDomain(domain, mealFoodMap);
    }

    private Food fetchFood(Long foodId) {
        return foodRepository.findById(foodId)
                .orElseThrow(() -> new FoodNotFoundException(String.format(FOOD_NOT_FOUND, foodId)));
    }

    @Transactional
    public List<ResponseMealDTO> findAllUserMeals(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
        }
        List<Meal> allUserMeals = mealRepository.findAllByUserId(userId);
        return allUserMeals.stream().map(e -> responseMealMapper.fromDomain(e, (fetchMapOfFood(e)))).toList();
    }

    private Map<Food, Double> fetchMapOfFood(Meal meal) {
        List<MealFood> allByMealId = mealFoodRepository.findAllByMealId(meal.getId());
        Map<Food, Double> mealFoodMap = new HashMap<>();
        allByMealId.forEach(e -> mealFoodMap.put(e.getFood(), e.getFoodQuantity()));
        return mealFoodMap;
    }
}
