package Cafe.CafeOrderSystem.Menu;

import Cafe.CafeOrderSystem.CatalogItems.*;
import Cafe.CafeOrderSystem.Menu.Items.*;

import java.util.*;

public class MenuManagement {
    private final CafeMenu cafeMenu;
    private final BeverageModifier beverageModifier;
    private final PastriesModifier pastriesModifier;

    public MenuManagement(CafeMenu cafeMenu){
        this.cafeMenu = cafeMenu;
        this.beverageModifier = new BeverageModifier();
        this.pastriesModifier = new PastriesModifier();
    }

    public List<BeverageItem> getBeverageItems() {
        return cafeMenu.getBeverageItems();
    }

    public List<PastriesItem> getPastriesItems(){
        return cafeMenu.getPastriesItems();
    }

    public List<CustomItem> getAddOnItems(){
        return cafeMenu.getBeverageAddOn();
    }

    public BeverageItem getBeverageItem(int idx){
        return cafeMenu.retrieveBeverageItem(idx);
    }

    public PastriesItem getPastriesItem(int idx){
        return cafeMenu.retrievePastriesItem(idx);
    }

    public CustomItem getAddOnItem(int idx){
        return cafeMenu.retrieveCustomItem(idx);
    }

    public void modifyBeverageSize(int beverageIdx, BeverageSize size,
                                   Map<Ingredients, Integer> ingredientCost, double price){

        validateIngredientCost(ingredientCost);
        validatePrice(price);
        BeverageItem beverageItem = getBeverageItem(beverageIdx);
        BeverageItem modifiedItem = beverageModifier.modifyItemSize(size, ingredientCost,
                beverageItem, price);
        cafeMenu.removeItem(beverageIdx);
        cafeMenu.addNewItem(modifiedItem);
    }

    public void removeBeverageSize(int beverageIdx, BeverageSize size){
        BeverageItem beverageItem = getBeverageItem(beverageIdx);
        BeverageItem modifiedItem = beverageModifier.removeItemSize(size, beverageItem);
        cafeMenu.removeItem(beverageIdx);
        cafeMenu.addNewItem(modifiedItem);
    }

    public void removeBeverageItem(int beverageIdx){
        cafeMenu.removeItem(beverageIdx);
    }

    public void modifyPastriesCost(int pastriesIdx, double price){
        validatePrice(price);
        PastriesItem item = getPastriesItem(pastriesIdx);
        List<PastriesItem> pastries = getPastriesItems();
        pastriesModifier.modifyPastriesCost(pastries, item, price);
    }

    public void modifyPastriesIngredientCost(Map<Ingredients, Integer> newIngredientCost,
                                             int pastriesIdx){

        validateIngredientCost(newIngredientCost);
        PastriesItem item = getPastriesItem(pastriesIdx);
        List<PastriesItem> pastries = getPastriesItems();
        pastriesModifier.modifyPastriesIngredientsCost(pastries,  item, newIngredientCost);
    }

    private void validateIngredientCost(Map<Ingredients, Integer> ingredientCost){
        ingredientCost.forEach((key, value) -> {
            if (key == null || value == null){
                throw new IllegalArgumentException(
                        String.format("Ingredient %s cannot be null or have null amount", key)
                );
            } else if (value <= 0){
                throw new IllegalArgumentException(
                        String.format("Ingredient %s cannot be negative value of %d",
                                key, value)
                );
            }
        });
    }

    private void validatePrice(double price){
        if (price <= 0){
            throw new IllegalArgumentException(
                    String.format("Expected a positive value for price. Got %.2f", price)
            );
        }
    }
}
