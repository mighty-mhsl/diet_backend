package app.healthy.diet.service;

import app.healthy.diet.client.AnthropicClient;
import app.healthy.diet.model.MealPlan;
import app.healthy.diet.model.Meal;
import app.healthy.diet.repository.MealRepository;
import app.healthy.diet.mapper.MealMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealPlanService {
    private final AnthropicClient anthropicClient;
    private final ObjectMapper objectMapper;
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    private static final String MEAL_PLAN_PROMPT_TEMPLATE = """
            # Role
            You are a nutritionist and meal planner specializing in the Mediterranean diet and also on the recommendations given in the book "The Food Compass". 
            Your task is to create a healthy meal plan for the user.\n
            
            # Details
            The meal plan should include a variety of meals that are healthy, balanced, and nutritious according to the Mediterranean diet principles and the guidelines from "The Food Compass".
            The meals should be diverse, incorporating a wide range of ingredients, and should not repeat the same meal within the plan.\n
            The daily total cooking time for the meal plan should not exceed 90 minutes.\n
            The ingredients may be found in a typical grocery store and should be easy to prepare.\n
            The recipe field in the response should include step-by-step instructions for preparing the meal.\n
            
            # Task
            Generate a healthy meal plan between %s and %s.\n
            
            # Format
            Return only JSON in the following structure:\n
            {\n  \"meals\": [\n    {\n      \"id\": 0,\n      \"name\": \"\",\n      \"mealType\": \"BREAKFAST|LUNCH|DINNER|BITE\",\n      \"description\": \"\",\n      \"healthBenefits\": \"\",\n      \"cookingTime\": 0,\n      \"leftover\": false,\n      \"ingredients\": [ { \"name\": \"\", \"quantity\": \"\", \"unit\": \"\" } ],\n      \"recipe\": \"\"\n    }\n  ]\n}\n
            
            Don't add ```json``` or any other formatting to the JSON response. Just return the JSON object as is.\n
            """;

    public MealPlan getCurrentMealPlan() throws IOException {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(2);

        if (mealRepository.existsByCookDate(start)) {
            var entities = mealRepository.findWithIngredientsBetween(start, end);
            var meals = entities.stream().map(mealMapper::toDto).toList();
            int total = meals.stream().mapToInt(Meal::getCookingTime).sum();
            return new MealPlan(0, start, end, meals, total);
        }

        String prompt = buildPrompt(start, end);
        String completion = anthropicClient.complete(prompt);
        MealPlan plan = objectMapper.readValue(completion, MealPlan.class);
        int total = plan.getMeals().stream().mapToInt(Meal::getCookingTime).sum();
        plan.setStartDate(start);
        plan.setEndDate(end);
        plan.setTotalCookingTime(total);

        for (Meal meal : plan.getMeals()) {
            meal.setCookDate(start);
        }

        var entities = plan.getMeals().stream()
                .map(mealMapper::toEntity)
                .toList();

        mealRepository.saveAll(entities);

        return plan;
    }


    private String buildPrompt(LocalDate start, LocalDate end) {
        return String.format(MEAL_PLAN_PROMPT_TEMPLATE, start, end);
    }
}
