package Cafe.CafeOrderSystem.Ingredients;

import Cafe.CafeOrderSystem.JsonParser.ItemsParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;

public class IngredientList extends JsonCollection<Ingredient> {
    /**
     * This class is responsible for creating and managing a collection of Ingredients
     * @param fileParser The relevant parser for the object
     * @param folderPath The folder from which the collection fills and writes to
     */
    public IngredientList(ItemsParser fileParser, String folderPath) {
        super(fileParser, folderPath, Ingredient.class);
    }

    public boolean modifyQuantity(Ingredient targetIngre, int amount){
        int ingrLoc = this.findObject(targetIngre);
        if (ingrLoc == -1){//checks if ingredient is in collection
            return false; //item doesn't exist
        }

        Ingredient tempIngr = (getObject(ingrLoc));// creates temp ingredient
        int tempQuant = tempIngr.getQuantity(); // gets its quantity
        tempQuant += amount; // increments it by input
        tempIngr.setQuantity(tempQuant);// sets new quant

        return true; //function succeeded
    }
}
