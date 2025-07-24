package Cafe.CafeOrderSystem.JsonParser.CafeMenu;

import Cafe.CafeOrderSystem.JsonParser.CafeObjectParser;
import Cafe.CafeOrderSystem.JsonParser.JsonArrayParser;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import com.fasterxml.jackson.databind.*;

import java.util.*;
import java.io.*;

public class PastriesParser implements CafeObjectParser<PastriesItem> {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public PastriesParser() {}

    @Override
    public List<PastriesItem> getItems(File jsonFile) {
        List<JsonNode> items = JsonArrayParser.parse(jsonFile);
        return parseItems(items, jsonFile);
    }

    private List<PastriesItem> parseItems(List<JsonNode> rootNode, File file){
        if (rootNode.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Json file %s does not contain any rootNode", file.getAbsolutePath()
                    ));
        }

        List<PastriesItem> pastriesItems = new ArrayList<>();
        for (JsonNode node : rootNode) {
            PastriesItem item = getItem(node, file);
            validatePastries(item);
            pastriesItems.add(item);
        }

        return pastriesItems;
    }

    private PastriesItem getItem(JsonNode node, File file){
        if (node == null){
            throw new IllegalArgumentException(
                    String.format("Json file %s contains a null node", file.getAbsolutePath()
                    ));
        }

        try {
            return MAPPER.treeToValue(node, PastriesItem.class);
        } catch (Exception e){
            throw new IllegalArgumentException(
                    String.format("Could not parse the Pastry Item. Reason: %s", e.getMessage()),e
            );
        }
    }

    private void validatePastries(PastriesItem item){
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
