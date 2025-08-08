package Cafe.CafeOrderSystem.Inventory;

import Cafe.CafeOrderSystem.Inventory.Ingredients.Ingredient;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientList;
import Cafe.CafeOrderSystem.JsonParser.ItemsParser;

public class Inventory {
    private IngredientList inventoryQuant;
    private String inventoryPath = "";

    /**
     * class is responsible for holding a collection of Ingredients, and the modification of
     * the list
     */
    public Inventory(){
        ItemsParser parser = new ItemsParser();
        this.inventoryQuant = new IngredientList(parser, inventoryPath);
        inventoryQuant.startCollection();//automatically populates inventory
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

    /**
     * Saves data to Json Files
     */
    public void shutDownInventory(){
        inventoryQuant.endCollection();
    }


}
