package Cafe.CafeOrderSystem.Inventory;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Ingredients.IngredientItem;

import java.util.*;

public class CafeInventory {
    private final Map<Ingredients, Integer> inventory;
    private final Map<String,Ingredients> ingredientCatalog;

    public CafeInventory(){
        inventory = new HashMap<>();
        ingredientCatalog = new HashMap<>();
    }

    /**
     * This function adds an ingredient to the
     * @param ingredient
     * @return
     */
    protected boolean addIngredient(IngredientItem ingredient){
        if (inventory.containsKey(ingredient.getIngredient())){
            throw new IllegalArgumentException(
                    String.format("Ingredient %s already exists in the inventory", ingredient)
            );
        }
        inventory.put(ingredient.getIngredient(), ingredient.getAmount());
        ingredientCatalog.put(ingredient.getId(), ingredient.getIngredient());
        return true;
    }

    protected boolean removeIngredient(Ingredients ingredient){
        if (inventory.containsKey(ingredient)){
            inventory.remove(ingredient);
            return true;
        }
        return false;
    }

    protected boolean updateIngredientAmount(String id, int amount){
        Ingredients ingredient = getIngredient(id);
        return updateIngredientAmount(ingredient, amount);
    }

    private Ingredients getIngredient(String id){
        if (!ingredientCatalog.containsKey(id)){
            throw new IllegalArgumentException(
                    String.format("There is no Ingredient in inventory with id '%s'", id)
            );
        }
        return ingredientCatalog.get(id);
    }

    private boolean updateIngredientAmount(Ingredients ingredient, int amount){
        inventory.put(ingredient, inventory.get(ingredient) + amount);
        return true;
    }

    public Map<Ingredients, Integer> getInventory(){
        return inventory;
    }
}
