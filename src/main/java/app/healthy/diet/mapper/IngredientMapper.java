package app.healthy.diet.mapper;

import app.healthy.diet.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    Ingredient toDto(app.healthy.diet.entity.Ingredient entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "meal", ignore = true)
    app.healthy.diet.entity.Ingredient toEntity(Ingredient ingredient);
}
