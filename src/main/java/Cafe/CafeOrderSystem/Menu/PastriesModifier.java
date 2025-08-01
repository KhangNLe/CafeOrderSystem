package Cafe.CafeOrderSystem.Menu;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Menu.Items.*;

import java.util.*;

/**
 * {@code PastriesModifier} is responsible for creating modified copies of {@link PastriesItem}
 * instances with updated cost or ingredient information.
 * <p>
 * All modifications produce new immutable {@code PastriesItem} objects, preserving
 * the original instance and enabling safe reuse.
 * <p>
 * This class encapsulates the logic for:
 * <ul>
 *   <li>Updating the price of a pastry item while keeping ingredients intact</li>
 *   <li>Updating the ingredient costs while keeping the price intact</li>
 * </ul>
 *
 * <p>Note: Validation of inputs (e.g., positive price, non-null ingredients) is expected
 * to be performed by the caller before invoking these methods.
 *
 * @see PastriesItem
 * @see PastriesCost
 * @see Ingredients
 */
public class PastriesModifier {

    /**
     * Returns a new {@link PastriesItem} with an updated price but the same ingredient costs
     * as the original.
     *
     * @param targetItem the pastry item to modify
     * @param price the new price to set
     * @return a new {@code PastriesItem} instance with the updated price
     */
    public PastriesItem modifyPastriesCost(PastriesItem targetItem, double price){
        PastriesCost newPrice = getNewCost(targetItem.cost().ingredients(), price);
        return replacePastriesCost(targetItem, newPrice);
    }

    /**
     * Returns a new {@link PastriesItem} with updated ingredient costs but the same price
     * as the original.
     *
     * @param targetItem the pastry item to modify
     * @param ingredientCost the new ingredient cost map to set
     * @return a new {@code PastriesItem} instance with the updated ingredient costs
     */
    public PastriesItem modifyPastriesIngredientsCost(PastriesItem targetItem,
                                              Map<Ingredients, Integer> ingredientCost){
        PastriesCost newCost = getNewCost(ingredientCost, targetItem.cost().price());
        return replacePastriesCost(targetItem, newCost);
    }

    /**
     * Helper method to create a new {@link PastriesCost} instance from given ingredients and price.
     *
     * @param ingredientsCost the ingredient cost map
     * @param newPrice the price to set
     * @return a new {@code PastriesCost} object
     */
    private PastriesCost getNewCost(Map<Ingredients, Integer> ingredientsCost, double newPrice){
        return new PastriesCost(
                newPrice,
                ingredientsCost
        );
    }

    /**
     * Helper method to create a new {@link PastriesItem} with updated cost information.
     *
     * @param item the original pastry item
     * @param cost the new {@code PastriesCost} to associate
     * @return a new {@code PastriesItem} instance with updated cost
     */
    private PastriesItem replacePastriesCost(PastriesItem item, PastriesCost cost){
        return new PastriesItem(
                item.id(),
                item.name(),
                item.type(),
                cost
        );
    }
}