package Cafe.CafeOrderSystem.Menu;

import Cafe.CafeOrderSystem.CatalogItems.*;
import Cafe.CafeOrderSystem.Menu.Items.BeverageCost;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;

import java.util.*;

/**
 * {@code BeverageModifier} is responsible for updating the size-specific cost and
 * ingredient information of {@link BeverageItem} objects.
 * <p>
 * All modifications return new immutable instances of {@code BeverageItem},
 * preserving the original object and ensuring referential safety.
 * <p>
 * This class encapsulates logic for:
 * <ul>
 *     <li>Adding or updating a beverage size entry</li>
 *     <li>Removing a beverage size entry</li>
 * </ul>
 *
 * <p>Note: This class does not perform validation; caller is expected to ensure input integrity.
 *
 * @see BeverageItem
 * @see BeverageSize
 * @see BeverageCost
 */
public class BeverageModifier {


    /**
     * Creates a modified copy of the given {@link BeverageItem}, adding or replacing the
     * specified {@link BeverageSize} with a new {@link BeverageCost} constructed from the
     * given ingredient map and price.
     *
     * @param size the beverage size to add or update
     * @param ingredients the ingredient cost mapping for the new size
     * @param targetItem the original beverage item to modify
     * @param price the price to associate with the size
     * @return a new {@code BeverageItem} with the updated size-cost mapping
     */
    public BeverageItem modifyItemSize(BeverageSize size, Map<Ingredients, Integer> ingredients,
                               BeverageItem targetItem, double price){
        Map<BeverageSize, BeverageCost> costs = new HashMap<>(targetItem.cost());
        BeverageCost sizeCost = new BeverageCost(price, ingredients);
        costs.put(size, sizeCost);
        return new BeverageItem(targetItem.id(), targetItem.name(), targetItem.type(), costs);
    }

    /**
     * Creates a modified copy of the given {@link BeverageItem}, removing the specified
     * {@link BeverageSize} entry if it exists.
     *
     * @param size the size variant to remove
     * @param targetItem the beverage item to modify
     * @return a new {@code BeverageItem} without the specified size variant
     */
    public BeverageItem removeItemSize(BeverageSize size, BeverageItem targetItem){

        Map<BeverageSize, BeverageCost> costs = new HashMap<>(targetItem.cost());
        costs.remove(size);
        return  new BeverageItem(targetItem.id(), targetItem.name(), targetItem.type(), costs);
    }
}
