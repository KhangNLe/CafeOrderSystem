package Cafe.CafeOrderSystem.JsonParser.CafeMenu;

import Cafe.CafeOrderSystem.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Inventory.InventoryRegister;
import Cafe.CafeOrderSystem.JsonParser.JsonArrayParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class InventoryParser {
    private InventoryParser() {}
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String RESOURCE = "src/main/resources/InitialCatalog/InitialInventory.json";

    public static void initializeCafeInventory() {
        List<JsonNode> rootNode = JsonArrayParser.parse(RESOURCE);
        InventoryRegister manager = new InventoryRegister();
        manager.initializeInventory();
        getIngredients(rootNode, manager);

    }

    private static void getIngredients(List<JsonNode> rootNode, InventoryRegister manager) {
        if (rootNode.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("File at %s does not contain any Json information.", RESOURCE)
            );
        }

        for (JsonNode node : rootNode) {
            IngredientItem item = getIngredient(node);
            validateIngredient(item);
            manager.addIngredient(item);
        }
    }

    private static IngredientItem getIngredient(JsonNode node) {
        if (node == null || node.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Could not get Ingredient Item from %s. Node was null or empty",
                            RESOURCE
            ));
        }

        try {
            return MAPPER.treeToValue(node, IngredientItem.class);
        } catch (Exception e) {
            String itemID = (node.hasNonNull("id"))? node.get("id").asText() : "unknown";
            throw new IllegalArgumentException(
                    String.format("Item with %s could not be parse into an Ingredient Item from file %s",
                            itemID, RESOURCE
            ));
        }
    }

    private static void validateIngredient(IngredientItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Ingredient item cannot be null.");
        }

        if (item.id() == null || item.id().isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Ingredient item in %s contain a null or empty id.", RESOURCE)
            );
        }

        if (item.ingredient() == null) {
            throw new IllegalArgumentException(
               String.format("Ingredient item with id %s at file %s contain a null ingredient",
                       item.id(), RESOURCE
            ));
        }

        if (item.amount() <= 0){
            throw new IllegalArgumentException(
                    String.format("Ingredient %s with id %s contain an amount value less than or equal to 0 of %d",
                            item.ingredient(), item.id(), item.amount()
            ));
        }
    }
}
