package app.healthy.diet.entity;

import app.healthy.diet.model.MealType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

import app.healthy.diet.entity.Ingredient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private String description;

    private String healthBenefits;

    private int cookingTime;

    private boolean isLeftover;

    private boolean cooked;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Ingredient> ingredients;

    private String recipe;

    private LocalDate cookDate;
}

