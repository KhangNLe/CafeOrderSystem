package Cafe.CafeOrderSystem.Inventory.Ingredients;

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

    public boolean modifyQuantity(IngredientItem targetIngre, int amount){
        int ingrLoc = this.findObject(targetIngre);
        if (ingrLoc == -1){//checks if ingredient is in collection
            return false; //item doesn't exist
        }

        IngredientItem tempIngr = (getObject(ingrLoc));// creates temp ingredients
        int tempQuant = tempIngr.getAmount(); // gets its quantity
        tempQuant += amount; // increments it by input
        if (tempQuant >= 0) {//validating modification amount
            tempIngr.changeAmount(tempQuant);// sets new quant
        }else {
            return false; //error message
        }

        return true; //function succeeded
    }
}