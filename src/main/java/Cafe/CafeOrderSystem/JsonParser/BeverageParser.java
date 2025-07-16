package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Menu.CafeMenu;
import Cafe.CafeOrderSystem.Menu.Items.BeverageCost;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class BeverageParser {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private BeverageParser() {}

    public static void initializeMenuBeverages(String filePath) {
        CafeMenu menu = CafeMenu.getInstance();
        List<JsonNode> rootNodes = JsonArrayParser.parse(filePath);
        getBeverageItems(rootNodes, menu, filePath);
    }

    private static void getBeverageItems(List<JsonNode> rootNode, CafeMenu menu, String filePath) {
        if (rootNode.isEmpty()) {
            throw new IllegalArgumentException("There was no beverage items in the JSON file: " + filePath);
        }

        for (JsonNode node : rootNode) {
            BeverageItem item = getItem(node);
            validateBeverageItem(item, filePath);
            menu.addBeverages(item);
        }
    }

    private static BeverageItem getItem(JsonNode node){
        if (node == null || node.isEmpty()){
            throw new IllegalArgumentException("Json File contains a null node");
        }

        try {
            return MAPPER.treeToValue(node, BeverageItem.class);
        } catch (Exception e) {
            String itemId = node.get("id").asText();
            throw new IllegalArgumentException(
                    String.format("Item with id %s does not follow the Beverage Item format", itemId)
            );
        }
    }

    private static void validateBeverageItem(BeverageItem item, String filePath) {
        if (item == null){
            throw new IllegalArgumentException("Beverage item is null for file: " + filePath);
        }

        if (item.name() == null || item.name().isEmpty() || item.id() == null || item.id().isEmpty()
        || item.type() == null){
            throw new IllegalArgumentException("Beverage item is null for file: " + filePath);
        }

        Map<BeverageSize, BeverageCost> cost = item.cost();
        validateItemCost(cost, item.id());

    }

    private static void validateItemCost(Map<BeverageSize, BeverageCost> cost, String itemId){
        if (cost == null || cost.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Beverage item id %s does not contain any listing price", itemId)
            );
        }

        cost.forEach((size, beverageCost) -> {
            if (beverageCost.price() <= 0){
                throw new IllegalArgumentException(
                        String.format("Drink size %s has a price less than 0. Got %.2f",
                                size, beverageCost.price()
                        ));
            }

            Map<Ingredients, Integer> ingredients = beverageCost.ingredients();
            validateIngredients(ingredients, itemId, size);

        });
    }

    private static void validateIngredients(Map<Ingredients, Integer> ingredients, String itemId, BeverageSize size) {
        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Beverage item id %s , size %s does not contain any ingredients",
                            itemId, size
                    ));
        }

        ingredients.forEach((ingredient, quantity) -> {
            if (quantity < 0){
                throw new IllegalArgumentException(
                        String.format("Ingredient: %s in item id %s contain a negative quantity of %d",
                                itemId, ingredient, quantity)
                );
            }
        });
    }
}
