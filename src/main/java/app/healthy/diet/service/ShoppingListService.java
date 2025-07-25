package app.healthy.diet.service;

import app.healthy.diet.client.AnthropicClient;
import app.healthy.diet.mapper.ShoppingItemMapper;
import app.healthy.diet.model.ShoppingItem;
import app.healthy.diet.model.ShoppingList;
import app.healthy.diet.exception.EntityNotFoundException;
import app.healthy.diet.repository.ShoppingItemRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListService {
    private final AnthropicClient anthropicClient;
    private final ObjectMapper objectMapper;
    private final ShoppingItemRepository shoppingItemRepository;
    private final ShoppingItemMapper shoppingItemMapper;

    private static final String SHOPPING_LIST_PROMPT_TEMPLATE = """
            You are a helpful assistant. Use the provided meals JSON to build a consolidated shopping list.\n
            # Task\n
            Combine all ingredients from the meals, merge duplicates and sum the required quantities.\n
            # Format\n            Return only JSON in the following structure:\n
            [\n  {\n    \"id\": 0,\n    \"ingredientName\": \"\",\n    \"quantity\": \"\",\n    \"unit\": \"\",\n    \"isPurchased\": false,\n    \"estimatedCost\": \"\"\n  }\n]\n
            Don't add ```json``` or any other formatting to the JSON response.\n
            Meals JSON:\n%s\n
            """;

    public void generateAndSave(LocalDate planDate, String mealsJson) throws IOException {
        String prompt = String.format(SHOPPING_LIST_PROMPT_TEMPLATE, mealsJson);
        String completion = anthropicClient.complete(prompt);
        List<ShoppingItem> items = objectMapper.readValue(
                completion,
                new TypeReference<List<ShoppingItem>>() {}
        );
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
        if (planDate.plusDays(2).isBefore(today)) {
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
