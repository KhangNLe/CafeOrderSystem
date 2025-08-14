package Cafe.CafeOrderSystem.Inventory;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientList;
import Cafe.CafeOrderSystem.JsonParser.ItemsParser;

import java.util.*;

public class Inventory  {
    private IngredientList inventoryQuant;

    /**
     * class is responsible for holding a collection of Ingredients, and the modification of
     * the list
     */
    public Inventory(IngredientList inList){
        ItemsParser parser = new ItemsParser();
        this.inventoryQuant = inList;
    }

    /**
     * Modifies and verifies quantity to modify
     * @param changeAmount modification amount
     * @param desiredIngr ingredient to modify
     * @return
     */
    public boolean modifyInventory(int changeAmount, IngredientItem desiredIngr){
        return inventoryQuant.modifyQuantity(desiredIngr, changeAmount);
    }

    public boolean modifyInventory(Ingredients ingredient, int amount){
        return inventoryQuant.modifyQuantity(ingredient, amount);
    }

    /**
     * Returns the list
     */
    public IngredientList getList(){
        return this.inventoryQuant;
    }

}
