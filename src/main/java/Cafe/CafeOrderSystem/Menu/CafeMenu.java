package Cafe.CafeOrderSystem.Menu;

import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.AddOnParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.BeverageParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.PastriesParser;
import Cafe.CafeOrderSystem.Menu.Items.*;

import java.util.*;

public class CafeMenu {
    private final BeverageParser beverageItems;
    private final PastriesParser pastryItems;
    private final AddOnParser beverageAddOn;

    public CafeMenu(BeverageParser beverageItems,
                     PastriesParser pastryItems, AddOnParser beverageAddOn){
        this.beverageItems = beverageItems;
        this.pastryItems =  pastryItems;
        this.beverageAddOn = beverageAddOn;
    }

    public List<BeverageItem> getBeverageItems() {
        return beverageItems.getCollection();
    }

    public List<PastriesItem> getPastriesItems() {
        return pastryItems.getCollection();
    }

    public List<CustomItem> getBeverageAddOn() {
        return beverageAddOn.getCollection();
    }

    public void removeBeverageItem(int itemIdx){
        beverageItems.removeObject(itemIdx);
    }

    public void addNewBeverageItem(BeverageItem beverageItem){
        beverageItems.addObject(beverageItem);
    }

    public void removePastriesItem(int itemIdx){
        pastryItems.removeObject(itemIdx);
    }

    public void addPastiesItem(PastriesItem item){
        pastryItems.addObject(item);
    }

    public BeverageItem retrieveBeverageItem(int index){
        BeverageItem item = beverageItems.getObject(index);
        if (item == null){
            throw new InvalidInputException(
                    String.format("Beverage item of index %d does not exist!", index)
            );
        }

        return item;
    }

    public PastriesItem retrievePastriesItem(int index){
        PastriesItem item = pastryItems.getObject(index);
        if (item == null){
            throw new InvalidInputException(
                    String.format("Pastries item of index %d does not exist!", index)
            );
        }
        return item;
    }

    public CustomItem retrieveCustomItem(int index){
        CustomItem item = beverageAddOn.getObject(index);
        if (item == null){
            throw new InvalidInputException(
                    String.format("Custom item of index %d does not exist!", index)
            );
        }
        return item;
    }
}