package Cafe.CafeOrderSystem.Menu;

import Cafe.CafeOrderSystem.JsonParser.CafeMenu.AddOnParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.BeverageParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.PastriesParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;
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

    public BeverageItem retrieveBeverageItem(int index){
        BeverageItem item = beverageItems.getObject(index);
        if (item == null){
            throw new IllegalArgumentException(
                    String.format("Beverage item of index %d does not exist!", index)
            );
        }

        return item;
    }
}