package Cafe.CafeOrderSystem.Menu;

import Cafe.CafeOrderSystem.CatalogItems.*;
import Cafe.CafeOrderSystem.Menu.Items.BeverageCost;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;

import java.util.*;

public class BeverageModifier {
    public BeverageItem modifyItemSize(BeverageSize size, Map<Ingredients, Integer> ingredients,
                               BeverageItem targetItem, double price){
        Map<BeverageSize, BeverageCost> costs = new HashMap<>(targetItem.cost());
        BeverageCost sizeCost = new BeverageCost(price, ingredients);
        costs.put(size, sizeCost);
        return new BeverageItem(targetItem.id(), targetItem.name(), targetItem.type(), costs);
    }

    public BeverageItem removeItemSize(BeverageSize size, BeverageItem targetItem){

        Map<BeverageSize, BeverageCost> costs = new HashMap<>(targetItem.cost());
        costs.remove(size);
        return  new BeverageItem(targetItem.id(), targetItem.name(), targetItem.type(), costs);
    }
}
