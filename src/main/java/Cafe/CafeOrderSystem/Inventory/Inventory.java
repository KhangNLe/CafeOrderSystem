package Cafe.CafeOrderSystem.Inventory;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientList;
import Cafe.CafeOrderSystem.JsonParser.ItemsParser;

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
        if(!inventoryQuant.modifyQuantity(desiredIngr, changeAmount)){
            return false; //error message
        }

        return true; //successful modification
    }

    /**
     * Returns the list
     */
    public IngredientList getList(){
        return this.inventoryQuant;
    }

}
