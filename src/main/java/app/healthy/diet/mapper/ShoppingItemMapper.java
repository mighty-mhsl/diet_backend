package app.healthy.diet.mapper;

import app.healthy.diet.model.ShoppingItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShoppingItemMapper {
    ShoppingItem toDto(app.healthy.diet.entity.ShoppingItem entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "planDate", ignore = true)
    app.healthy.diet.entity.ShoppingItem toEntity(ShoppingItem item);
}
