package app.healthy.diet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "diet.meal-plan")
public class MealPlanProperties {
    private int generationDays = 2;

    private int exclusionDays = 3;
}

