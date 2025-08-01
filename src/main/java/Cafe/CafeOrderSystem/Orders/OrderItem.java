package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import Cafe.CafeOrderSystem.Menu.Items.CustomItem;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * Represents a menu item in an order, including its base configuration and pricing.
 * <p>
 * This class includes ingredient cost breakdown, supports customization through
 * {@link CustomItem}, and ensures type-safe modifications.
 * </p>
 *
 * <p>
 * Instances of this class are immutable by field declaration for most properties,
 * but note: the `ingredientsCost` map is modified during customization, meaning this class
 * is not fully immutable. If stricter immutability is desired, consider defensive copying.
 * </p>
 *
 * <p>
 * This class is JSON-deserializable and ignores unknown properties during deserialization.
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItem {
    private final String itemID;
    private final String itemName;
    private final MenuType itemType;
    private final Map<Ingredients, Integer> ingredientsCost;
    private double price;

    /**
     * Constructs an {@code OrderItem} from serialized JSON.
     *
     * @param itemID           unique identifier for the item
     * @param itemName         name of the item
     * @param itemType         type/category of the item (e.g., BEVERAGE, PASTRY)
     * @param ingredientsCost  map representing ingredient types and quantities
     * @param price            base price of the item
     */
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

    /**
     * Applies a {@link CustomItem} modification to this order item, altering
     * its ingredients and updating the total price accordingly.
     * <p>
     * Modifications are performed *in-place*, violating immutability. Ensure this is intentional.
     * </p>
     *
     * @param item the custom modification to apply
     * @throws IllegalArgumentException if the modification is not compatible with the item type
     */
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

    /**
     * Validates that the provided {@link CustomItem} is applicable to the type of this order item.
     *
     * @param item the custom item to validate
     * @throws IllegalArgumentException if the item type is incompatible
     */
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
