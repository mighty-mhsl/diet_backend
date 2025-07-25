package app.healthy.diet.mapper;

import app.healthy.diet.model.InventoryItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryItemMapper {
    InventoryItem toDto(app.healthy.diet.entity.InventoryItem entity);

    @Mapping(target = "id", ignore = true)
    app.healthy.diet.entity.InventoryItem toEntity(InventoryItem item);
}
