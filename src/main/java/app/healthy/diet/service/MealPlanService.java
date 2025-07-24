package app.healthy.diet.service;

import app.healthy.diet.client.AnthropicClient;
import app.healthy.diet.model.MealPlan;
import app.healthy.diet.model.Meal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MealPlanService {
    private final AnthropicClient anthropicClient;
    private final ObjectMapper objectMapper;

    private static final String MEAL_PLAN_PROMPT_TEMPLATE = """
Generate a healthy meal plan between %s and %s.\n
Return only JSON in the following structure:\n
{\n  \"meals\": [\n    {\n      \"id\": 0,\n      \"name\": \"\",\n      \"mealType\": \"BREAKFAST|LUNCH|DINNER|BITE\",\n      \"description\": \"\",\n      \"healthBenefits\": \"\",\n      \"cookingTime\": 0,\n      \"isLeftover\": false,\n      \"ingredients\": [ { \"name\": \"\", \"quantity\": \"\", \"unit\": \"\" } ],\n      \"recipe\": \"\"\n    }\n  ]\n}\n""";

    public MealPlan getCurrentMealPlan() throws IOException {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(2);
        String prompt = buildPrompt(start, end);
        String completion = anthropicClient.complete(prompt);
        MealPlan plan = objectMapper.readValue(completion, MealPlan.class);
        int total = plan.getMeals().stream().mapToInt(Meal::getCookingTime).sum();
        plan.setStartDate(start);
        plan.setEndDate(end);
        plan.setTotalCookingTime(total);
        return plan;
    }

    private String buildPrompt(LocalDate start, LocalDate end) {
        return String.format(MEAL_PLAN_PROMPT_TEMPLATE, start, end);
    }
}
