package com.crud.api.service;

import com.crud.api.dto.RequestFoodDTO;
import com.crud.api.dto.ResponseFoodDTO;
import com.crud.api.entity.Food;
import com.crud.api.entity.User;
import com.crud.api.error.UserNotFoundException;
import com.crud.api.mapper.RequestFoodMapper;
import com.crud.api.mapper.ResponseFoodMapper;
import com.crud.api.repository.FoodFactRepository;
import com.crud.api.repository.FoodRepository;
import com.crud.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private static final String USER_NOT_FOUND = "User with id: %s not found";

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final RequestFoodMapper requestFoodMapper;
    private final FoodFactRepository foodFactRepository;
    private final ResponseFoodMapper responseFoodMapper;

    public ResponseFoodDTO createFood(RequestFoodDTO dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));
        Food foodDomain = requestFoodMapper.toDomain(dto);
        foodDomain.setUser(user);
        foodFactRepository.save(foodDomain.getFoodFact());
        foodRepository.save(foodDomain);
        return responseFoodMapper.fromDomain(foodDomain);
    }

    public List<ResponseFoodDTO> findUserFoods(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userId));
        }
        List<Food> allByUserId = foodRepository.findAllByUserId(userId);
        return allByUserId.stream()
                .map(responseFoodMapper::fromDomain)
                .toList();
    }
}
