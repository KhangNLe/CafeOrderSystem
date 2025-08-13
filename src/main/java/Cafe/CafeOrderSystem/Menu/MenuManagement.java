package Cafe.CafeOrderSystem.Menu;

import Cafe.CafeOrderSystem.CatalogItems.*;
import Cafe.CafeOrderSystem.Exceptions.*;
import Cafe.CafeOrderSystem.Menu.Items.*;

import java.util.*;

/**
 * {@code MenuManagement} acts as the main interface for performing business operations
 * on the cafe menu, such as modifying beverage sizes, updating pastries, and validating
 * ingredient costs or prices. It encapsulates the {@link CafeMenu} domain object and
 * coordinates modifications through dedicated {@link BeverageModifier} and {@link PastriesModifier}
 * utilities.
 * <p>
 * This class ensures proper validation before any updates are applied to the menu data.
 * All mutations are performed in a controlled and validated manner to maintain domain integrity.
 *
 * <p>Typical usage:
 * <pre>
 *     MenuManagement management = new MenuManagement(cafeMenu);
 *     management.modifyBeverageSize(0, BeverageSize.LARGE, costMap, 6.50);
 * </pre>
 */
public class MenuManagement {
    private final CafeMenu cafeMenu;
    private final BeverageModifier beverageModifier;
    private final PastriesModifier pastriesModifier;

    /**
     * constructs a new {@code MenuManagement}object with the give {@link CafeMenu}
     *
     * @param cafeMenu the cafe menu domain object to manage
     */
    public MenuManagement(CafeMenu cafeMenu){
        this.cafeMenu = cafeMenu;
        this.beverageModifier = new BeverageModifier();
        this.pastriesModifier = new PastriesModifier();
    }

    /**
     * Returns the list of available beverage items.
     *
     * @return list of beverages in the menu
     */
    public List<BeverageItem> getBeverageItems() {
        return cafeMenu.getBeverageItems();
    }

    /**
     * Returns the list of available pastries.
     *
     * @return list of pastries in the menu
     */
    public List<PastriesItem> getPastriesItems(){
        return cafeMenu.getPastriesItems();
    }

    /**
     * Returns the list of beverage add-ons
     *
     * @return list of beverage customization items
     */
    public List<CustomItem> getAddOnItems(){
        return cafeMenu.getBeverageAddOn();
    }

    /**
     * Retrieves a specific beverage item by its index in the list
     *
     * @param idx the index of the beverage item
     * @return the {@link BeverageItem} at the specified index
     * @throws InvalidInputException if the index is invalid
     */
    public BeverageItem getBeverageItem(int idx){
        return cafeMenu.retrieveBeverageItem(idx);
    }

    /**
     * Retrieves a specific pastry item by its index in the list
     *
     * @param idx index of the pastry item
     * @return the {@link PastriesItem} at the specified index
     * @throws InvalidInputException if the index is invalid
     */
    public PastriesItem getPastriesItem(int idx){
        return cafeMenu.retrievePastriesItem(idx);
    }

    public Map<BeverageItem, List<CustomItem>> getBeverageWithCustomizeOption(){
        Map<BeverageItem, List<CustomItem>> items = new HashMap<>();

        initializeMap(items, cafeMenu.getBeverageItems());
        getAddOnForBeverage(items, cafeMenu.getBeverageAddOn());

        return items;
    }

    /**
     * Retrieves a specific add-on item by its index
     *
     * @param idx index of the custom item
     * @return the {@link CustomItem} at the specified index
     * @throws InvalidInputException if the index is invalid
     */
    public CustomItem getAddOnItem(int idx){
        return cafeMenu.retrieveCustomItem(idx);
    }

    /**
     * Modifies the ingredient cost and price for a specific beverage size
     * Replaces the existing beverage entry with an updated version
     *
     * @param beverageIdx index of the beverage item to modify
     * @param size size to modify
     * @param ingredientCost ingredient requirements for the beverage size
     * @param price the new price for the size
     * @throws InvalidModifyingException if the ingredient map or price is invalid
     */
    // public void modifyBeverageSize(int beverageIdx, BeverageSize size,
    //                                Map<Ingredients, Integer> ingredientCost, double price){

    //     validateIngredientCost(ingredientCost);
    //     validatePrice(price);
    //     BeverageItem beverageItem = getBeverageItem(beverageIdx);
    //     BeverageItem modifiedItem = beverageModifier.modifyItemSize(size, ingredientCost,
    //             beverageItem, price);
    //     cafeMenu.removeBeverageItem(beverageIdx);
    //     cafeMenu.addNewBeverageItem(modifiedItem);
    // }

    //TREVOR: I added these so I could do more streamlined adding and removing, also to normalize the two
    public void modifyBeverageSize(int beverageIdx, BeverageSize size,
                                   Map<Ingredients, Integer> ingredientCost, double price) {
        validateIngredientCost(ingredientCost);
        validatePrice(price);
        BeverageItem beverageItem = getBeverageItem(beverageIdx);

        BeverageItem modifiedItem = beverageModifier.modifyItemSize(size, ingredientCost,
                beverageItem, price);

        cafeMenu.setBeverageItem(beverageIdx, modifiedItem);
    }

    public void modifyPastryItem(int pastriesIdx,
                                 Map<Ingredients, Integer> ingredientCost,
                                 double price) {
        validateIngredientCost(ingredientCost);
        validatePrice(price);

        PastriesItem base = getPastriesItem(pastriesIdx);
        PastriesItem modified = pastriesModifier.modifyPastriesIngredientsCost(base, ingredientCost);
        modified = pastriesModifier.modifyPastriesCost(modified, price);

        cafeMenu.setPastryItem(pastriesIdx, modified);
    }

    /**
     * Removes a beverage size from the specified beverage item
     *
     * @param beverageIdx index of the beverage item
     * @param size size to remove
     * @throws InvalidInputException if the beverage item does not exist
     */
    public void removeBeverageSize(int beverageIdx, BeverageSize size){
        BeverageItem beverageItem = getBeverageItem(beverageIdx);
        BeverageItem modifiedItem = beverageModifier.removeItemSize(size, beverageItem);

        cafeMenu.setBeverageItem(beverageIdx, modifiedItem);
    }

    /**
     * Removes the entire beverage item from the menu
     *
     * @param beverageIdx index of the beverage item to remove
     */
    public void removeBeverageItem(int beverageIdx){
        cafeMenu.removeBeverageItem(beverageIdx);
    }


    /**
     * Modifies the price of a specific pastry item
     *
     * @param pastriesIdx index of the pastry item
     * @param price new price to set
     * @throws InvalidModifyingException if the price is invalid
     */    public void modifyPastriesCost(int pastriesIdx, double price){
        validatePrice(price);
        PastriesItem item = getPastriesItem(pastriesIdx);
        PastriesItem newItem = pastriesModifier.modifyPastriesCost(item, price);

        cafeMenu.setPastryItem(pastriesIdx, newItem);
    }

    /**
     * Modifies the ingredient cost of a specific pastry item
     *
     * @param newIngredientCost updated ingredient requirements
     * @param pastriesIdx index of the pastry item
     * @throws InvalidModifyingException if the ingredient map is invalid
     */
    public void modifyPastriesIngredientCost(Map<Ingredients, Integer> newIngredientCost,
                                             int pastriesIdx) {

        validateIngredientCost(newIngredientCost);
        PastriesItem item = getPastriesItem(pastriesIdx);
        PastriesItem newItem = pastriesModifier.modifyPastriesIngredientsCost(item,
                newIngredientCost);

        cafeMenu.setPastryItem(pastriesIdx, newItem);
    }



    /**
     * Validates that all ingredient quantities are non-null and greater than zero
     *
     * @param ingredientCost the ingredient cost map to validate
     * @throws InvalidModifyingException if entry is invalid
     */
    private void validateIngredientCost(Map<Ingredients, Integer> ingredientCost) {
        ingredientCost.forEach((key, value) -> {
            if (key == null || value == null){
                throw new InvalidModifyingException(
                        String.format("Ingredient %s cannot be null or have null amount", key)
                );
            } else if (value < 0){
                throw new InvalidModifyingException(
                        String.format("Ingredient %s cannot be negative value of %d",
                                key, value)
                );
            }
        });
    }

    /**
     * Validates that the price is a positive number
     *
     * @param price the price to validate
     * @throws InvalidModifyingException if the price is not positive
     */
    private void validatePrice(double price){
        if (price <= 0){
            throw new InvalidModifyingException(
                    String.format("Expected a positive value for price. Got %.2f", price)
            );
        }
    }

    private void initializeMap(Map<BeverageItem, List<CustomItem>> items,
                               List<BeverageItem> beverageItems){
        beverageItems.forEach(item -> {
            items.put(item, new ArrayList<>());
        });
    }

    private void getAddOnForBeverage(Map<BeverageItem, List<CustomItem>> items,
                                     List<CustomItem> addOnItems){
        for (BeverageItem beverage : items.keySet()) {
            List<CustomItem> addOns = new ArrayList<>();

            for (CustomItem customItem : addOnItems) {
                if (customItem.applicableTo().equals(beverage.type())){
                    addOns.add(customItem);
                }
            }

            items.put(beverage, addOns);
        }
    }
}