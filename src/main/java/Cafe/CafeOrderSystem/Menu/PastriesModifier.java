package Cafe.CafeOrderSystem.Menu;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Menu.Items.*;

import java.util.*;

public class PastriesModifier {
    public void modifyPastriesCost(List<PastriesItem> menuPastries, PastriesItem targetItem,
                                   double price){
        PastriesCost newPrice = getNewCost(targetItem.cost().ingredients(), price);
        PastriesItem item = replacePastriesCost(targetItem, newPrice);

        menuPastries.remove(targetItem);
        menuPastries.add(item);
    }

    public void modifyPastriesIngredientsCost(List<PastriesItem> menuPastries,
                                              PastriesItem targetItem,
                                              Map<Ingredients, Integer> ingredientCost){
        PastriesCost newCost = getNewCost(ingredientCost, targetItem.cost().price());
        PastriesItem item = replacePastriesCost(targetItem, newCost);

        menuPastries.remove(targetItem);
        menuPastries.add(item);
    }

    private PastriesCost getNewCost(Map<Ingredients, Integer> ingredientsCost, double newPrice){
        return new PastriesCost(
                newPrice,
                ingredientsCost
        );
    }

    private PastriesItem replacePastriesCost(PastriesItem item, PastriesCost cost){
        return new PastriesItem(
                item.id(),
                item.name(),
                item.type(),
                cost
        );
    }
}
