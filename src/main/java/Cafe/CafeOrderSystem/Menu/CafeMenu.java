package Cafe.CafeOrderSystem.Menu;

import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.AddOnParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.BeverageParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.PastriesParser;
import Cafe.CafeOrderSystem.Menu.Items.*;

import java.util.*;

/**
 * Facade class that provides access to the cafe menu, including beverages,
 * pastries, and add-ons.
 *
 * <p>This class wraps lower-level parsers and exposes high-level operations
 * for retrieving, adding, and removing menu items.</p>
 *
 * <p>All item retrievals are index-based and will throw {@link InvalidInputException}
 * if the item does not exist at the given index.</p>
 */
public class CafeMenu {
    private final BeverageParser beverageItems;
    private final PastriesParser pastryItems;
    private final AddOnParser beverageAddOn;

    /**
     * Constructs a {@code CafeMenu} instance with given parsers.
     *
     * @param beverageItems the parser managing beverage items
     * @param pastryItems the parser managing pastry items
     * @param beverageAddOn the parser managing beverage add-ons
     */
    public CafeMenu(BeverageParser beverageItems,
                     PastriesParser pastryItems, AddOnParser beverageAddOn){
        this.beverageItems = beverageItems;
        this.pastryItems =  pastryItems;
        this.beverageAddOn = beverageAddOn;
    }

    /**
     * Returns the list of all available beverage items.
     *
     * @return list of beverage items
     */
    public List<BeverageItem> getBeverageItems() {
        return beverageItems.getCollection();
    }

    /**
     * Returns the list of all available pastry items
     *
     * @return list of pastry items
     */
    public List<PastriesItem> getPastriesItems() {
        return pastryItems.getCollection();
    }

    /**
     * Returns the list of all available beverage add-ons.
     *
     * @return list of beverage add-on
     */
    public List<CustomItem> getBeverageAddOn() {
        return beverageAddOn.getCollection();
    }

    /**
     * Removes a beverage item by index
     *
     * @param itemIdx the index of the item to remove
     */
    public void removeBeverageItem(int itemIdx){
        beverageItems.removeObject(itemIdx);
    }

    /**
     * Adds a new beverage item
     *
     * @param beverageItem the beverage item to add
     */
    public void addNewBeverageItem(BeverageItem beverageItem){
        beverageItems.addObject(beverageItem);
    }

    /**
     * Removes a pastry item by index.
     *
     * @param itemIdx the index of the item to remove
     */
    public void removePastriesItem(int itemIdx){
        pastryItems.removeObject(itemIdx);
    }

    /**
     * Adds a new pastry item
     *
     * @param item the pastry item to add
     */
    public void addPastriesItem(PastriesItem item){
        pastryItems.addObject(item);
    }
    

    /**
     * Retrieves a beverage item by index
     *
     * @param index index of the item
     * @return the beverage item
     * @throws InvalidInputException if no item exists at the give index
     */
    public BeverageItem retrieveBeverageItem(int index){
        BeverageItem item = beverageItems.getObject(index);
        if (item == null){
            throw new InvalidInputException(
                    String.format("Beverage item of index %d does not exist!", index)
            );
        }

        return item;
    }

    /**
     * Retrieves a pastry item by index
     *
     * @param index index of the item
     * @return the pastry item
     * @throws InvalidInputException if no item exists at the given index
     */
    public PastriesItem retrievePastriesItem(int index){
        PastriesItem item = pastryItems.getObject(index);
        if (item == null){
            throw new InvalidInputException(
                    String.format("Pastries item of index %d does not exist!", index)
            );
        }
        return item;
    }

    /**
     * Retrieves a custom item (add-on) by index.
     *
     * @param index index of the item
     * @return the custom item
     * @throws InvalidInputException if no item exists at the given index
     */
    public CustomItem retrieveCustomItem(int index){
        CustomItem item = beverageAddOn.getObject(index);
        if (item == null){
            throw new InvalidInputException(
                    String.format("Custom item of index %d does not exist!", index)
            );
        }
        return item;
    }

    public void setBeverageItem(int index, BeverageItem item) {
        List<BeverageItem> items = getBeverageItems();

        if (index < 0 || index >= items.size()) {
            throw new InvalidInputException("Invalid beverage index: " + index);
        }

        items.set(index, item);
    }

    public void setPastryItem(int index, PastriesItem item) {
        List<PastriesItem> items = getPastriesItems();

        if (index < 0 || index >= items.size()) {
            throw new InvalidInputException("Invalid pastry index: " + index);
        }

        items.set(index, item);
    }
}