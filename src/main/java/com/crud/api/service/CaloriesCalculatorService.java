package com.crud.api.service;

import com.crud.api.dto.ResponseMeasurementDTO;
import com.crud.api.dto.ResponseNutritionDTO;
import com.crud.api.dto.UserGoalsResponseDTO;
import com.crud.api.entity.Measurement;
import com.crud.api.entity.User;
import com.crud.api.enums.Activity;
import com.crud.api.enums.Gender;
import com.crud.api.enums.MeasureType;
import com.crud.api.enums.Unit;
import com.crud.api.error.MeasurementNotFoundException;
import com.crud.api.error.UserNotFoundException;
import com.crud.api.mapper.ResponseMeasurementMapper;
import com.crud.api.repository.MeasurementRepository;
import com.crud.api.repository.UserRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.crud.api.util.ConstantsUtils.*;
import static com.crud.api.util.GenderUtils.*;

@Service
@RequiredArgsConstructor
public class CaloriesCalculatorService {
    private static final float SURPLUS = 0.2f;
    private static final float DEFICIT = 0.2f;

    private final UserRepository userRepository;
    private final MeasurementRepository measurementRepository;
    private final NutritionService nutritionService;
    private final ResponseMeasurementMapper responseMeasurementMapper;

    public UserGoalsResponseDTO calculate(Long userId, String goal) {
        if (goal.equals(MeasureType.ENERGY_TDEE.toString())) {
            return calculateTdee(userId);
        } else if (goal.equals(MeasureType.ENERGY_SURPLUS.toString())) {
            return calculateSurplus(userId);
        } else {
            return calculateDeficit(userId);
        }
    }

    public UserGoalsResponseDTO calculateTdee(Long userId) {

        /* fetch User from database */
        User user = fetchUser(userId);
        Measurement currentWeight = fetchUserCurrentWeight(userId);

        /* calculate BMR */
        double bmr = calculateBasalMetabolicRate(user);

        /* calculate tdee */
        double tdee = calculateTotalDailyEnergyExpenditure(bmr, user.getActivity());

        /* store User's TDEE in database */
        Measurement tdeeEntity = createTdeeEntity(tdee);

        return getUserGoalsResponseDTO(user, currentWeight, tdeeEntity);
    }

    private Measurement fetchUserCurrentWeight(Long userId) {
        List<Measurement> list = fetchAllMeasurementsByUserId(userId)
                .stream()
                .filter(e -> e.getType().equals(MeasureType.CURRENT_WEIGHT))
                .sorted(Comparator.comparing(Measurement::getId).reversed())
                .toList();

        if (list.isEmpty()) {
            throw new MeasurementNotFoundException(String.format(MEASUREMENT_TYPE_NOT_FOUND, MeasureType.CURRENT_WEIGHT, userId));
        }
        return list.get(0);
    }

    private double calculateBasalMetabolicRate(User user) {

        /* fetch User's necessary credentials */
        List<Measurement> measurementsList = fetchAllMeasurementsByUserId(user.getId());

        /* fetch User's current weight */
        Measurement userWeight = extractUserMeasurement(measurementsList, MeasureType.CURRENT_WEIGHT);

        /* fetch User's height */
        Measurement userHeight = extractUserMeasurement(measurementsList, MeasureType.HEIGHT);

        double weight = userWeight.getValue();
        double height = userHeight.getValue();
        int age = user.getAge();

        return user.getGender() == Gender.MALE ?
                maleCalculate(weight, height, age) :
                femaleCalculate(weight, height, age);
    }

    private Measurement extractUserMeasurement(List<Measurement> measurementsList, MeasureType measureType) {
        List<Measurement> list = measurementsList.stream()
                .filter(e -> e.getType().equals(measureType))
                .sorted(Comparator.comparing(Measurement::getId))
                .toList();
        if (list.isEmpty()) {
            throw new MeasurementNotFoundException(String.format(MEASUREMENT_NOT_FOUND));
        }
        return list.get(0);
    }

    private double maleCalculate(double weight, double height, int age) {
        return (MALE_CONST_1 +
                (MALE_CONST_W * weight) +
                (MALE_CONST_H * height) -
                (MALE_CONST_A * age));
    }

    private double femaleCalculate(double weight, double height, int age) {
        return (FEMALE_CONST_1 +
                (FEMALE_CONST_W * weight) +
                (FEMALE_CONST_H * height) -
                (FEMALE_CONST_A * age));
    }

    private double calculateTotalDailyEnergyExpenditure(double bmr, Activity activity) {
        return bmr * activity.getFactor();
    }

    private Measurement createTdeeEntity(double tdee) {
        Measurement tdeeEntity = new Measurement();
        tdeeEntity.setType(MeasureType.ENERGY_TDEE);
        tdeeEntity.setUnit(Unit.CALORIES);
        tdeeEntity.setValue((double) Math.round(tdee));
        tdeeEntity.setDate(LocalDate.now());
        return tdeeEntity;
    }


    public UserGoalsResponseDTO calculateSurplus(Long userId) {

        /* fetch User from database  */
        User user = fetchUser(userId);
        Measurement currentWeight = fetchUserCurrentWeight(userId);

        /* fetch tdee */
        double tdee = resolveLastUserTdee(user);

        /* calculate surplus */
        double surplus = tdee + tdee * SURPLUS;

        /* store user's surplus in database */
        Measurement surplusEntity = createSurplusEntity(surplus);

        return getUserGoalsResponseDTO(user, currentWeight, surplusEntity);
    }

    private Measurement createSurplusEntity(double surplus) {
        Measurement surplusEntity = new Measurement();
        surplusEntity.setType(MeasureType.ENERGY_SURPLUS);
        surplusEntity.setUnit(Unit.CALORIES);
        surplusEntity.setValue((double) Math.round(surplus));
        surplusEntity.setDate(LocalDate.now());
        return surplusEntity;
    }


    public UserGoalsResponseDTO calculateDeficit(Long userId) {

        /*  fetch User from database  */
        User user = fetchUser(userId);
        Measurement currentWeight = fetchUserCurrentWeight(userId);

        /* fetch tdee */
        double tdee = resolveLastUserTdee(user);

        /* calculate deficit */
        double deficit = tdee - tdee * DEFICIT;

        /* store user's deficit in database */
        Measurement deficitEntity = createDeficitEntity(deficit);

        return getUserGoalsResponseDTO(user, currentWeight, deficitEntity);
    }

    private ResponseNutritionDTO calculateNutritions(Measurement goal) {
        return nutritionService.calculateDefaultNutritions(goal);
    }

    private double resolveLastUserTdee(User user) {
        Optional<Float> lastUserTdee = fetchLastTdee(user.getId());
        double tdee;
        if (lastUserTdee.isEmpty()) {

            /* calculate BMR */
            double bmr = calculateBasalMetabolicRate(user);

            /* calculate tdee */
            tdee = calculateTotalDailyEnergyExpenditure(bmr, user.getActivity());

            /* store User's TDEE in database */
            Measurement tdeeEntity = createTdeeEntity(tdee);
            tdeeEntity.setUser(user);
            measurementRepository.save(tdeeEntity);
        } else {
            tdee = lastUserTdee.get();
        }
        return tdee;
    }

    private User fetchUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));
    }

    private Optional<Float> fetchLastTdee(Long userId) {
        return measurementRepository.findLastTdeeByUserId(userId);
    }

    private Measurement createDeficitEntity(double deficit) {
        Measurement deficitEntity = new Measurement();
        deficitEntity.setType(MeasureType.ENERGY_DEFICIT);
        deficitEntity.setUnit(Unit.CALORIES);
        deficitEntity.setValue((double) Math.round(deficit));
        deficitEntity.setDate(LocalDate.now());
        return deficitEntity;
    }

    private List<Measurement> fetchAllMeasurementsByUserId(Long userId) {
        return measurementRepository.findAllByUserId(userId);
    }

    private UserGoalsResponseDTO getUserGoalsResponseDTO(User user, Measurement currentWeight, Measurement goal) {
        goal.setUser(user);
        measurementRepository.save(goal);

        ResponseNutritionDTO nutrition = calculateNutritions(goal);

        ResponseMeasurementDTO goalResponseDTO = responseMeasurementMapper.fromDomain(goal);
        ResponseMeasurementDTO weightResponseDTO = responseMeasurementMapper.fromDomain(currentWeight);

        return UserGoalsResponseDTO.builder()
                .activity(user.getActivity())
                .goal(goalResponseDTO)
                .currentWeight(weightResponseDTO)
                .nutrition(nutrition)
                .build();
    }
}