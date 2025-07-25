package app.healthy.diet.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meal {
    private long id;

    private String name;

    private MealType mealType;

    private String description;

    private String healthBenefits;

    private int cookingTime;

    private boolean leftover;

    private List<Ingredient> ingredients;

    private String recipe;

    private LocalDate cookDate;
}
