package app.healthy.diet.repository;

import app.healthy.diet.entity.ShoppingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {
    Optional<ShoppingItem> findFirstByPlanDateLessThanEqualOrderByPlanDateDesc(LocalDate date);
    List<ShoppingItem> findByPlanDate(LocalDate planDate);
}
