package app.healthy.diet.repository;

import app.healthy.diet.entity.Meal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MealRepository extends JpaRepository<Meal, Long> {
    boolean existsByCookDate(LocalDate cookDate);
    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.ingredients WHERE m.cookDate BETWEEN :start AND :end")
    List<Meal> findWithIngredientsBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT m.name FROM Meal m WHERE m.cookDate BETWEEN :start AND :end")
    List<String> findNamesByCookDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.ingredients WHERE m.id = :id")
    java.util.Optional<Meal> findWithIngredientsById(@Param("id") Long id);
}

