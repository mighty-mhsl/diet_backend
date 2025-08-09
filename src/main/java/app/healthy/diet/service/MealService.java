package app.healthy.diet.service;

import app.healthy.diet.mapper.MealMapper;
import app.healthy.diet.model.Meal;
import app.healthy.diet.repository.MealRepository;
import app.healthy.diet.repository.InventoryItemRepository;
import app.healthy.diet.entity.InventoryItem;
import app.healthy.diet.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;
    private final InventoryItemRepository inventoryRepository;

    public Meal getMealById(Long id) {
        return mealMapper.toDto(getMealEntity(id));
    }

    @Transactional
    public void cookMeal(Long id) {
        var meal = getMealEntity(id);

        meal.setCooked(true);
        mealRepository.save(meal);

        if (meal.getIngredients() == null) {
            return;
        }

        var ingredientNames = meal.getIngredients().stream()
                .map(i -> i.getName())
                .toList();

        Map<String, InventoryItem> inventory = inventoryRepository.findByIngredientNameIn(ingredientNames).stream()
                .collect(Collectors.toMap(InventoryItem::getIngredientName, Function.identity()));

        meal.getIngredients().forEach(ingredient -> {
            var item = inventory.get(ingredient.getName());
            if (item == null) {
                return;
            }
            double remaining = item.getGrams() - ingredient.getGrams();
            if (remaining <= 0) {
                inventoryRepository.delete(item);
            } else {
                item.setGrams(remaining);
                inventoryRepository.save(item);
            }
        });
    }

    private app.healthy.diet.entity.Meal getMealEntity(Long id) {
        return mealRepository.findWithIngredientsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id " + id));
    }
}
