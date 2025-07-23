package app.healthy.diet.controller;

import app.healthy.diet.model.MealPlan;
import app.healthy.diet.service.MealPlanService;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meal-plans")
@RequiredArgsConstructor
public class MealPlanController {

    private final MealPlanService mealPlanService;

    @GetMapping("/current")
    public ResponseEntity<MealPlan> getCurrent() throws IOException {
        MealPlan plan = mealPlanService.getCurrentMealPlan();
        return ResponseEntity.ok(plan);
    }
}
