package Cafe.CafeOrderSystem.JsonParser.CafeMenu;

import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.JsonParser.ItemsParser;
import Cafe.CafeOrderSystem.JsonParser.JsonArrayParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class InventoryParser extends JsonCollection<IngredientItem> {
    private InventoryParser(ItemsParser parser, String dirPath, Class<IngredientItem> itemClass) {
        super(parser, dirPath, itemClass);
    }

    public static InventoryParser create(String dirPath){
        return new InventoryParser(new ItemsParser(), dirPath, IngredientItem.class);
    }
}
