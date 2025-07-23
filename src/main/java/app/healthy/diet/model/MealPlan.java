package app.healthy.diet.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealPlan {
    private long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Meal> meals;
    private int totalCookingTime;
}
