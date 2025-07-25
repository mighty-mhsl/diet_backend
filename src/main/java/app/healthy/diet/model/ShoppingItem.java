package app.healthy.diet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingItem {
    private long id;
    private String ingredientName;
    private String quantity;
    private String unit;
    private boolean purchased;
    private String estimatedCost;
}
