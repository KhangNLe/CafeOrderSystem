package Cafe.CafeOrderSystem.JsonParser.CafeMenu;

import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.JsonParser.CafeObjectParser;
import Cafe.CafeOrderSystem.JsonParser.JsonArrayParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class InventoryParser implements CafeObjectParser<IngredientItem> {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private String resource;

    public InventoryParser() {}

    @Override
    public List<IngredientItem> getItems(File jsonFile) {
        List<JsonNode> rootNode = JsonArrayParser.parse(jsonFile);
        this.resource = jsonFile.getAbsolutePath();
        return getIngredients(rootNode, jsonFile);
    }

    private List<IngredientItem> getIngredients(List<JsonNode> rootNode, File file) {
        if (rootNode.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("File at %s does not contain any Json information.", resource)
            );
        }

        List<IngredientItem> ingredients = new ArrayList<>();

        for (JsonNode node : rootNode) {
            IngredientItem item = getIngredient(node);
            validateIngredient(item);
            ingredients.add(item);
        }
        return ingredients;
    }

    private IngredientItem getIngredient(JsonNode node) {
        if (node == null || node.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Could not get Ingredient Item from %s. Node was null or empty",
                            resource
            ));
        }

        try {
            return MAPPER.treeToValue(node, IngredientItem.class);
        } catch (IOException e) {
            String itemID = (node.hasNonNull("id"))? node.get("id").asText() : "unknown";
            throw new IllegalArgumentException(
                    String.format("Item with %s could not be parse into an Ingredient Item from file %s. Reason %s",
                            itemID, resource, e.getMessage()
            ));
        }
    }

    private void validateIngredient(IngredientItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Ingredient item cannot be null.");
        }

        if (item.getId() == null || item.getId().isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Ingredient item in %s contain a null or empty id.", resource)
            );
        }

        if (item.getIngredient() == null) {
            throw new IllegalArgumentException(
               String.format("Ingredient item with id %s at file %s contain a null ingredient",
                       item.getId(), resource
            ));
        }

        if (item.getAmount() <= 0){
            throw new IllegalArgumentException(
                    String.format("Ingredient %s with id %s contain an amount value less than or equal to 0 of %d",
                            item.getIngredient(), item.getId(), item.getAmount()
            ));
        }
    }
}
