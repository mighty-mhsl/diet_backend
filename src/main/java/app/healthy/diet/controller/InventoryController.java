package app.healthy.diet.controller;

import app.healthy.diet.model.ShoppingItem;
import app.healthy.diet.service.InventoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Void> addToInventory(@RequestBody InventoryRequest request) throws IOException {
        inventoryService.addItems(request.getItems());
        return ResponseEntity.ok().build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class InventoryRequest {
        private List<ShoppingItem> items;
    }
}
