package Cafe.CafeOrderSystem.JsonParser.CafeMenu;

import Cafe.CafeOrderSystem.CatalogItems.*;
import Cafe.CafeOrderSystem.JsonParser.CafeObjectParser;
import Cafe.CafeOrderSystem.JsonParser.JsonArrayParser;
import Cafe.CafeOrderSystem.Menu.Items.*;
import com.fasterxml.jackson.databind.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BeverageCustomizeParser implements CafeObjectParser<CustomItem> {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private BeverageCustomizeParser() {}

    @Override
    public List<CustomItem> getItems(File filePath){
        List<JsonNode> rootNodes = JsonArrayParser.parse(filePath);
        return getAddOnItems(rootNodes, filePath);
    }

    private List<CustomItem> getAddOnItems(List<JsonNode> rootNode, File filePath){
        if (rootNode == null || rootNode.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("File: %s does not contain any Json datas", filePath)
            );
        }

        List<CustomItem> items = new ArrayList<>();
        for (JsonNode node : rootNode) {
            CustomItem item = getAddOnItem(node, filePath);
            validateCustomItem(item);
            items.add(item);
        }

        return items;
    }

    private CustomItem getAddOnItem(JsonNode node, File filePath){
        if (node == null) {
            throw new IllegalArgumentException(
                    String.format("File: %s contain a null Json node", filePath.getAbsolutePath())
            );
        }

        try {
            return MAPPER.treeToValue(node, CustomItem.class);
        } catch (IOException e){
            String itemId = (node.hasNonNull("id"))? node.get("id").asText() : "unknown";
            throw new IllegalArgumentException(
                    String.format("Could not get a Custom Item from id %s at %s. Reason %s",
                            itemId, filePath.getAbsolutePath(), e.getMessage()
            ));
        }
    }

    private void validateCustomItem(CustomItem item){
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

    private void validateIngredients(CustomItem item){
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

    private void validateAddOnIngredient(Map<Ingredients, Integer> ingredients, String itemId){
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

    private void validateReplacementIngredient(Map<Ingredients, ReplaceIngredients> replacement, String itemId){
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
