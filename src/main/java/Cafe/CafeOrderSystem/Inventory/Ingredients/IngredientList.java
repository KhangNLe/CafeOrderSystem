package Cafe.CafeOrderSystem.Inventory.Ingredients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.JsonParser.ItemsParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;

public class IngredientList extends JsonCollection<IngredientItem> {
    /**
     * This class is responsible for creating and managing a collection of Ingredients
     * @param folderPath The folder from which the collection fills and writes to
     */
    public IngredientList(String folderPath) {
        super(new ItemsParser(), folderPath, IngredientItem.class);
    }

    public boolean modifyQuantity(IngredientItem targetIngre, int amount) {
        int ingrLoc = this.findObject(targetIngre);
        if (ingrLoc == -1) {
            return false; // item doesn't exist
        }

        IngredientItem tempIngr = getObject(ingrLoc);
        int currentAmount = tempIngr.getAmount();
        int newAmount = currentAmount + amount; // Properly adds/subtracts

        if (newAmount >= 0) {
            tempIngr.changeAmount(newAmount); // Set the new calculated amount
            return true;
        } else {
            System.out.println("Insufficient stock for " + targetIngre.getIngredient());
            return false;
        }
    }


    //Trevor: I added this

    public Map<IngredientItem, Integer> getIngredients() {
    // Hacky but works - JsonCollection stores items internally
    Map<IngredientItem, Integer> items = new HashMap<>();

        for (int i = 0; ; i++) {
            try {
            IngredientItem ingredientItem = getObject(i);
            int quantity = ingredientItem.getAmount();

            items.put(ingredientItem, quantity);
            
        } catch (Exception e) {
            break; // No more items
        }
    }

    return items;
}


}