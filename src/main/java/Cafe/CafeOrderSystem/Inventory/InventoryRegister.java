package Cafe.CafeOrderSystem.Inventory;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Ingredients.IngredientItem;

public class InventoryRegister {
    private CafeInventory inventory;

    public void initializeInventory() {
        inventory = CafeInventory.getInstance();
    }

    public boolean addIngredient(IngredientItem ingredient) {
        validateIngredient(ingredient);
        return inventory.addIngredient(ingredient);
    }

    public boolean reduceIngredientAmount(IngredientItem ingredient){
        validateIngredient(ingredient);
        return inventory.updateIngredientAmount(ingredient.id(), -ingredient.amount());
    }

    public boolean restockIngredientAmount(IngredientItem ingredient){
        validateIngredient(ingredient);
        return inventory.updateIngredientAmount(ingredient.id(), ingredient.amount());
    }

    public boolean removeIngredient(Ingredients ingredient){
        if (ingredient == null){
            throw new IllegalArgumentException("Ingredient cannot be null");
        }
        return inventory.removeIngredient(ingredient);
    }

    private void validateIngredient(IngredientItem ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Cannot add null ingredient to the inventory");
        }

        if (ingredient.ingredient() == null) {
            throw new IllegalArgumentException("Cannot add null ingredient to the inventory");
        }

        if (ingredient.amount() <= 0){
            throw new IllegalArgumentException(
                    String.format("Ingredient %s cannot have an amount less than or equal to 0 of %d",
                            ingredient.ingredient(), ingredient.amount()
            ));
        }
    }
}
