package app.healthy.diet.service;

import app.healthy.diet.model.MealPlan;
import app.healthy.diet.model.Meal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MealPlanService {
    private final AnthropicClient anthropicClient;
    private final PromptLayerClient promptLayerClient;
    private final ObjectMapper objectMapper;

    @Value("${promptlayer.meal-plan-id}")
    private String mealPlanPromptId;

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

    private String buildPrompt(LocalDate start, LocalDate end) throws IOException {
        String template = promptLayerClient.getPrompt(mealPlanPromptId);
        return template.replace("{{startDate}}", start.toString())
                .replace("{{endDate}}", end.toString());
    }
}
