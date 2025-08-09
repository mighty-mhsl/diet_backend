package app.healthy.diet.repository;

import app.healthy.diet.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByIngredientName(String ingredientName);

    List<InventoryItem> findByIngredientNameIn(Collection<String> ingredientNames);
}
