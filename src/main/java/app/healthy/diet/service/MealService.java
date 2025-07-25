package app.healthy.diet.service;

import app.healthy.diet.mapper.MealMapper;
import app.healthy.diet.model.Meal;
import app.healthy.diet.repository.MealRepository;
import app.healthy.diet.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    public Meal getMealById(Long id) {
        var entity = mealRepository.findWithIngredientsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id " + id));
        return mealMapper.toDto(entity);
    }
}
