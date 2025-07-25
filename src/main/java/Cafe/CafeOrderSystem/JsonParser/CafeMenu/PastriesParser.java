package Cafe.CafeOrderSystem.JsonParser.CafeMenu;

import Cafe.CafeOrderSystem.JsonParser.JsonArrayParser;
import Cafe.CafeOrderSystem.Menu.CafeMenu;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import com.fasterxml.jackson.databind.*;

import java.util.*;

public class PastriesParser {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private PastriesParser() {}

    public static void parsePastries(String filePath) {
        CafeMenu menu = CafeMenu.getInstance();
        List<JsonNode> items = JsonArrayParser.parse(filePath);
        parseItems(menu, items);
    }

    private static void parseItems(CafeMenu menu, List<JsonNode> items){
        if (items.isEmpty()){
            throw new IllegalArgumentException("Json file does not contain any items");
        }

        for (JsonNode node : items) {
            PastriesItem item = getItem(node);
            validatePastries(item);
            menu.addPastries(item);
        }
    }

    private static PastriesItem getItem(JsonNode node){
        if (node == null){
            throw new IllegalArgumentException("Json file contains a null node");
        }

        try {
            return MAPPER.treeToValue(node, PastriesItem.class);
        } catch (Exception e){
            throw new IllegalArgumentException(
                    String.format("Could not parse the Pastry Item. Reason: %s", e.getMessage()),e
            );
        }
    }

    private static void validatePastries(PastriesItem item){
        if (item == null){
            throw new IllegalArgumentException("Json parsed a null pastries item");
        }

        String itemID = item.id();
        if (item.cost().price() <= 0){
            throw new IllegalArgumentException(
                    String.format("Pastry Item id %s has a cost smaller than 0 (%.2f)",
                            itemID, item.cost().price()
            ));
        }
    }
}
