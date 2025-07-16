package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.CatalogItems.*;
import Cafe.CafeOrderSystem.Menu.CafeMenu;
import Cafe.CafeOrderSystem.Menu.Items.*;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.*;

public class BeverageCustomizeParser {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private BeverageCustomizeParser() {}

    public static void initializeBeverageAddOn(String filePath){
        CafeMenu cafe = CafeMenu.getInstance();
        List<JsonNode> rootNodes = JsonArrayParser.parse(filePath);
        getAddOnItems(rootNodes, cafe, filePath);
    }

    private static void getAddOnItems(List<JsonNode> rootNode, CafeMenu cafe, String filePath){
        if (rootNode == null || rootNode.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("File: %s does not contain any Json datas", filePath)
            );
        }

        for (JsonNode node : rootNode) {
            CustomItem item = getAddOnItem(node, cafe, filePath);
            validateCustomItem(item);
            cafe.addBeverageAddOn(item);
        }
    }

    private static CustomItem getAddOnItem(JsonNode node, CafeMenu cafe, String filePath){
        if (node == null) {
            throw new IllegalArgumentException(
                    String.format("File: %s contain a null Json node", filePath)
            );
        }

        try {
            return MAPPER.treeToValue(node, CustomItem.class);
        } catch (IOException e){
            String itemId = (node.hasNonNull("id"))? node.get("id").asText() : "unknown";
            throw new IllegalArgumentException(
                    String.format("Could not get a Custom Item from id %s at %s. Reason %s",
                            itemId, filePath, e.getMessage()
            ));
        }
    }

    private static void validateCustomItem(CustomItem item){
        if (item.id() == null || item.id().isEmpty() || item.name() == null || item.name().isEmpty()) {
            throw new IllegalArgumentException("Custom item contains a null or empty id or name");
        }

        if (item.additionalPrice() <=  0){
            throw new IllegalArgumentException(
                    String.format("Custom item contains a negative or 0 additional price of %.2f",
                            item.additionalPrice()
            ));
        }
        validateIngredients(item);

        if (item.applicableTo() == null || item.applicableTo().isEmpty()){
            throw new IllegalArgumentException(
              String.format("Custom item id %s does not contains a beverage applicable type", item.id()
            ));
        }
    }

    private static void validateIngredients(CustomItem item){
        if ((item.ingredients() == null || item.ingredients().isEmpty()) &&
                (item.ingredientReplacement() == null || item.ingredientReplacement().isEmpty())) {
            throw new IllegalArgumentException(
                    String.format("Custom item id %s have both null or empty ingredient or replacement ingredient",
                            item.id()
                    ));
        } else if (item.ingredients() == null){
            validateReplacementIngredient(item.ingredientReplacement(), item.id());
        } else if (item.ingredientReplacement() == null) {
            validateAddOnIngredient(item.ingredients(), item.id());
        }
    }

    private static void validateAddOnIngredient(Map<Ingredients, Integer> ingredients, String itemId){
        ingredients.forEach((stuff, amount) -> {
            if (stuff == null){
                throw new IllegalArgumentException(
                        String.format("Custom item id %s contain a null ingredient", itemId
                        ));
            } else if (amount == null || amount <= 0){
                throw new IllegalArgumentException(
                        String.format("Custom item id %s ingredient %s contain a null or negative amount",
                                itemId, stuff
                        ));
            }
        });
    }

    private static void validateReplacementIngredient(Map<Ingredients, ReplaceIngredients> replacement, String itemId){
        replacement.forEach((ingredient, newIngredient) -> {
            if (ingredient == null || newIngredient == null) {
                throw new IllegalArgumentException(
                        String.format("Custom item id %s contain a null ingredient or its replacement",
                                itemId
                        ));
            }

            if (newIngredient.replaceWith() == null){
                throw new IllegalArgumentException(
                        String.format("Custom item id %s contain a null replacement ingredient", itemId
                ));
            }
        });
    }
}
