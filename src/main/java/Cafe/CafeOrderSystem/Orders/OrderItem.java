package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import Cafe.CafeOrderSystem.Menu.Items.CustomItem;
import Cafe.CafeOrderSystem.Menu.Items.ReplaceIngredients;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItem {
    private final String itemID;
    private final String itemName;
    private final MenuType itemType;
    private final Map<Ingredients, Integer> ingredientsCost;
    private double price;

    @JsonCreator
    public OrderItem(
            @JsonProperty("itemID") String itemID,
            @JsonProperty("itemName") String itemName,
            @JsonProperty("itemType") MenuType itemType,
            @JsonProperty("ingredientsCost") Map<Ingredients, Integer> ingredientsCost,
            @JsonProperty("price") double price
    ){
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemType = itemType;
        this.ingredientsCost = ingredientsCost;
        this.price = price;
    }

    public void modifyOrderItem(CustomItem item) {
        validateCustomItem(item);

        if (item.ingredientReplacement() == null) {
            ingredientsCost.putAll(item.ingredients());
        } else {
            Map<Ingredients, ReplaceIngredients> replacements = item.ingredientReplacement();
            for (Ingredients target : replacements.keySet()) {
                if (ingredientsCost.containsKey(target)) {
                    ReplaceIngredients replace = replacements.get(target);
                    int amount = ingredientsCost.get(target);
                    ingredientsCost.put(replace.replaceWith(), amount);
                    ingredientsCost.remove(target);
                    break;
                }
            }
            price += item.additionalPrice();
        }
    }

    private void validateCustomItem(CustomItem item) {
        StringBuilder sb = new StringBuilder();
        if (!item.applicableTo().contains(itemType)) {
            item.applicableTo().forEach(i -> sb.append(i).append(" "));
            throw new IllegalArgumentException(
                    String.format("Item %s can only be applied to %s not %s",
                            item.name(), sb.toString(), item.type()
                    ));
        }
    }

    public String getItemID() {
        return itemID;
    }

    public double getPrice() {
        return price;
    }

    public MenuType getItemType() {
        return itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public Map<Ingredients, Integer> getIngredientsCost() {
        return ingredientsCost;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OrderItem{");
        sb.append("itemID='").append(itemID).append('\'');
        sb.append(", itemName='").append(itemName).append('\'');
        sb.append(", itemType=").append(itemType);
        sb.append(", ingredients:= ");
        ingredientsCost.forEach((ingredient, amount) -> {
            sb.append('(');
            sb.append(ingredient).append(":").append(amount);
            sb.append(')');
        });
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }

}
