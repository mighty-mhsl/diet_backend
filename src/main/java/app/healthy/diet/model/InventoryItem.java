package app.healthy.diet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItem {
    private long id;
    private String ingredientName;
    private String quantity;
    private String unit;
    private boolean isPurchased;
    private String estimatedCost;
    private LocalDate planDate;
    private LocalDate expirationDate;
}
