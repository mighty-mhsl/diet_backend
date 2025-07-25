package app.healthy.diet.controller;

import app.healthy.diet.model.ShoppingList;
import app.healthy.diet.service.ShoppingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shopping-lists")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    @GetMapping("/current")
    public ResponseEntity<ShoppingList> getCurrent() {
        ShoppingList list = shoppingListService.getCurrentShoppingList();
        return ResponseEntity.ok(list);
    }
}
