package app.healthy.diet.service;

import app.healthy.diet.client.AnthropicClient;
import app.healthy.diet.mapper.ShoppingItemMapper;
import app.healthy.diet.model.Ingredient;
import app.healthy.diet.model.Meal;
import app.healthy.diet.model.MealPlan;
import app.healthy.diet.model.ShoppingItem;
import app.healthy.diet.model.ShoppingList;
import app.healthy.diet.exception.EntityNotFoundException;
import app.healthy.diet.repository.InventoryItemRepository;
import app.healthy.diet.repository.ShoppingItemRepository;
import app.healthy.diet.config.MealPlanProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingListService {
    private final AnthropicClient anthropicClient;
    private final ObjectMapper objectMapper;
    private final ShoppingItemRepository shoppingItemRepository;
    private final ShoppingItemMapper shoppingItemMapper;
    private final InventoryItemRepository inventoryItemRepository;
    private final MealPlanProperties mealPlanProperties;

    private static final String PACK_SIZES_PROMPT_TEMPLATE = """
            You are a helpful assistant. For each ingredient name in the provided JSON array, provide the typical store package weight in grams.\n
            # Format\n            Return only JSON in the following structure:\n            [\n  {\n    \"ingredientName\": \"\",\n    \"grams\": 0\n  }\n]\n            Don't add ```json``` or any other formatting to the JSON response.\n            Ingredients JSON:\n%s\n""";

    @Transactional
    public void generateAndSave(LocalDate planDate, String mealsJson) throws IOException {
        MealPlan plan = objectMapper.readValue(mealsJson, MealPlan.class);

        Map<String, Double> needed = new HashMap<>();
        for (Meal meal : plan.getMeals()) {
            for (Ingredient ing : meal.getIngredients()) {
                needed.merge(ing.getName(), ing.getGrams(), Double::sum);
            }
        }

        var inventoryMap = inventoryItemRepository.findAll().stream()
                .collect(Collectors.toMap(i -> i.getIngredientName(), i -> i.getGrams(), Double::sum));

        needed.replaceAll((name, grams) -> grams - inventoryMap.getOrDefault(name, 0.0));

        List<String> toBuy = needed.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .toList();

        if (toBuy.isEmpty()) {
            return;
        }

        String ingredientsJson = objectMapper.writeValueAsString(toBuy);
        String prompt = String.format(PACK_SIZES_PROMPT_TEMPLATE, ingredientsJson);
        log.info("Requesting pack sizes: {}", ingredientsJson);
        String completion = anthropicClient.complete(prompt);
        log.info("Pack size completion: {}", completion);

        List<ShoppingItem> items = objectMapper.readValue(
                completion,
                new TypeReference<>() {}
        );

        items.forEach(item -> {
            double required = needed.getOrDefault(item.getIngredientName(), 0.0);
            double pack = item.getGrams();
            if (pack <= 0) {
                pack = required;
            }
            item.setGrams(Math.ceil(required / pack) * pack);
        });

        var entities = items.stream()
                .map(item -> {
                    var entity = shoppingItemMapper.toEntity(item);
                    entity.setPlanDate(planDate);
                    return entity;
                })
                .toList();
        shoppingItemRepository.saveAll(entities);
    }

    public ShoppingList getCurrentShoppingList() {
        LocalDate today = LocalDate.now();
        var firstOpt = shoppingItemRepository.findFirstByPlanDateLessThanEqualOrderByPlanDateDesc(today);
        if (firstOpt.isEmpty()) {
            throw new EntityNotFoundException("Shopping list not found for current plan");
        }
        LocalDate planDate = firstOpt.get().getPlanDate();
        if (planDate.plusDays(mealPlanProperties.getGenerationDays()).isBefore(today)) {
            throw new EntityNotFoundException("Shopping list not found for current plan");
        }
        var items = shoppingItemRepository.findByPlanDate(planDate).stream()
                .map(shoppingItemMapper::toDto)
                .toList();
        double total = items.stream()
                .mapToDouble(i -> {
                    String cost = i.getEstimatedCost();
                    if (cost == null || cost.isBlank()) {
                        return 0;
                    }
                    try {
                        return Double.parseDouble(cost);
                    } catch (NumberFormatException ex) {
                        return 0;
                    }
                })
                .sum();
        return new ShoppingList(0, items, total, planDate);
    }
}
