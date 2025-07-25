package app.healthy.diet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inventory")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ingredientName;
    private String quantity;
    private String unit;
    private boolean isPurchased;
    private String estimatedCost;
    private LocalDate planDate;
    private LocalDate expirationDate;
}
