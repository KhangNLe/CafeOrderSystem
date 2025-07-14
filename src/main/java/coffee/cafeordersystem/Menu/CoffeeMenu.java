package coffee.cafeordersystem.Menu;

import coffee.cafeordersystem.Menu.Items.*;

import java.util.*;

public class CoffeeMenu {
    private final String pastry = "PASTRIES";
    private final String beverage = "BEVERAGE";
    private static CoffeeMenu instance;
    private Map<String, BeverageItem> beveragesItems;
    private Map<String, PastriesItem> pastryItems;

    private CoffeeMenu(){
        this.beveragesItems = new HashMap<>();
        this.pastryItems = new HashMap<>();
    }

    public static CoffeeMenu getInstance(){
        if (instance == null){
            instance = new CoffeeMenu();
        }
        return instance;
    }

    public void addPastries(PastriesItem pastry){
        pastryItems.put(pastry.id(), pastry);
    }

    public void addBeverages(BeverageItem beverage){
        beveragesItems.put(beverage.id(), beverage);
    }
}
