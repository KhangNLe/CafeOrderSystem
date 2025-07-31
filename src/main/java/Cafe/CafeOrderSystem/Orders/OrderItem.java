package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import Cafe.CafeOrderSystem.Menu.Items.CustomItem;
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
            item.ingredients().forEach((ingredient, quantity) -> {
                if (ingredientsCost.containsKey(ingredient)) {
                    ingredientsCost.put(ingredient, ingredientsCost.get(ingredient) + quantity);
                } else {
                    ingredientsCost.put(ingredient, quantity);
                }
            });
        } else {
            item.ingredientReplacement().forEach((ingredient, replaceWith) -> {
               if (ingredientsCost.containsKey(ingredient)) {
                   int amount =  ingredientsCost.get(ingredient);
                   ingredientsCost.remove(ingredient);
                   ingredientsCost.put(replaceWith, amount);
               }
            });
        }

        price += item.additionalPrice();
    }

    private void validateCustomItem(CustomItem item) {
        StringBuilder sb = new StringBuilder();
        if (!item.applicableTo().contains(itemType)) {
            item.applicableTo().forEach(i -> sb.append(i).append(" "));
            throw new IllegalArgumentException(
                    String.format("Item %s can only be applied to %s not %s",
                            item.name(), sb.toString(), itemType
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
        return Collections.unmodifiableMap(ingredientsCost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem item)) return false;

        return  itemID.equals(item.itemID) && itemName.equals(item.itemName) &&
                itemType.equals(item.itemType) && ingredientsCost.equals(item.ingredientsCost) &&
                price == item.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemID, itemName, itemType, ingredientsCost, price);
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
