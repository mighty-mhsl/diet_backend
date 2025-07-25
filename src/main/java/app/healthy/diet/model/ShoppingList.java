package app.healthy.diet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingList {
    private long id;
    private List<ShoppingItem> items;
    private double estimatedCost;
    private LocalDate createdDate;
}
