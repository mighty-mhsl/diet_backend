package app.healthy.diet.service;

import app.healthy.diet.client.AnthropicClient;
import app.healthy.diet.mapper.InventoryItemMapper;
import app.healthy.diet.model.InventoryItem;
import app.healthy.diet.model.ShoppingItem;
import app.healthy.diet.repository.InventoryItemRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import app.healthy.diet.exception.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final AnthropicClient anthropicClient;
    private final ObjectMapper objectMapper;
    private final InventoryItemRepository inventoryRepository;
    private final InventoryItemMapper inventoryMapper;

    private static final String EXPIRATION_PROMPT_TEMPLATE = """
            You are a helpful assistant that estimates expiration dates for grocery items assuming ideal storage conditions.\n
            # Format\n            Return only JSON in the following structure:\n            [\n  {\n    \"id\": 0,\n    \"ingredientName\": \"\",\n    \"quantity\": \"\",\n    \"unit\": \"\",\n    \"purchased\": false,\n    \"estimatedCost\": \"\",\n    \"planDate\": \"YYYY-MM-DD\",\n    \"expirationDate\": \"YYYY-MM-DD\"\n  }\n]\n
            Don't add ```json``` or any other formatting to the JSON response.\n
            Shopping items JSON:\n%s\n""";

    @Transactional
    public void addItems(List<ShoppingItem> items) throws IOException {
        String itemsJson = objectMapper.writeValueAsString(items);
        String prompt = String.format(EXPIRATION_PROMPT_TEMPLATE, itemsJson);
        String completion = anthropicClient.complete(prompt);
        List<InventoryItem> inventoryItems = objectMapper.readValue(
                completion,
                new TypeReference<List<InventoryItem>>() {}
        );
        var entities = inventoryItems.stream()
                .map(inventoryMapper::toEntity)
                .toList();
        inventoryRepository.saveAll(entities);
    }

    public List<InventoryItem> getAllItems() {
        return inventoryRepository.findAll().stream()
                .map(inventoryMapper::toDto)
                .toList();
    }

    public void removeItem(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Inventory item not found with id " + id);
        }
        inventoryRepository.deleteById(id);
    }
}
