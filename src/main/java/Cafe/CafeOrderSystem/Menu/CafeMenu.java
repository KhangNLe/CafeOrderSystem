package Cafe.CafeOrderSystem.Menu;

import Cafe.CafeOrderSystem.Menu.Items.*;

import java.util.*;

public class CafeMenu {
    private static CafeMenu instance;
    private final Map<String, BeverageItem> beveragesItems;
    private final Map<String, PastriesItem> pastryItems;
    private final Map<String, CustomItem> beverageAddOn;

    private CafeMenu(){
        this.beveragesItems = new HashMap<>();
        this.pastryItems = new HashMap<>();
        this.beverageAddOn = new HashMap<>();
    }

    public static CafeMenu getInstance(){
        if (instance == null){
            instance = new CafeMenu();
        }
        return instance;
    }

    //This method is written in for a test
    public static void destroyInstance(){
        instance = null;
    }

    public Map<String, BeverageItem> getBeveragesItems() {
        return beveragesItems;
    }

    public Map<String, PastriesItem> getPastriesItems() {
        return pastryItems;
    }

    public Map<String, CustomItem> getBeverageAddOn() {
        return beverageAddOn;
    }

    public void addPastries(PastriesItem pastry){
        pastryItems.put(pastry.id(), pastry);
    }

    public void addBeverages(BeverageItem beverage){
        beveragesItems.put(beverage.id(), beverage);
    }

    public void addBeverageAddOn (CustomItem addOn){
        beverageAddOn.put(addOn.id(), addOn);
    }
}
