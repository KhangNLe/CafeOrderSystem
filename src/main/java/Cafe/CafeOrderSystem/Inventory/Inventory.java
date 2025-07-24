package Cafe.CafeOrderSystem.Inventory;

import Cafe.CafeOrderSystem.Ingredients.Ingredient;
import Cafe.CafeOrderSystem.Ingredients.IngredientList;
import Cafe.CafeOrderSystem.JsonParser.ItemsParser;

public class Inventory {
    private IngredientList inventoryQuant;
    private String inventoryPath = "";

    /**
     * class is responsible for holding a
     */
    public Inventory(){
        ItemsParser parser = new ItemsParser();
        this.inventoryQuant = new IngredientList(parser, inventoryPath);
        inventoryQuant.startCollection();
    }

    public void shutDownInventory(){
        inventoryQuant.endCollection();
    }


}
