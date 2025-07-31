package Cafe.CafeOrderSystem.Menu;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Menu.Items.*;

import java.util.*;

public class PastriesModifier {
    public PastriesItem modifyPastriesCost(PastriesItem targetItem, double price){
        PastriesCost newPrice = getNewCost(targetItem.cost().ingredients(), price);
        return replacePastriesCost(targetItem, newPrice);
    }

    public PastriesItem modifyPastriesIngredientsCost(PastriesItem targetItem,
                                              Map<Ingredients, Integer> ingredientCost){
        PastriesCost newCost = getNewCost(ingredientCost, targetItem.cost().price());
        return replacePastriesCost(targetItem, newCost);
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
