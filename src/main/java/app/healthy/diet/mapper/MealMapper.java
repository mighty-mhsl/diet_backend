package app.healthy.diet.mapper;

import app.healthy.diet.model.Meal;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = IngredientMapper.class)
public interface MealMapper {
    Meal toDto(app.healthy.diet.entity.Meal entity);

    @Mapping(target = "id", ignore = true)
    app.healthy.diet.entity.Meal toEntity(Meal meal);

    @AfterMapping
    default void link(@MappingTarget app.healthy.diet.entity.Meal meal) {
        if (meal.getIngredients() != null) {
            for (var ing : meal.getIngredients()) {
                ing.setMeal(meal);
            }
        }
    }
}
