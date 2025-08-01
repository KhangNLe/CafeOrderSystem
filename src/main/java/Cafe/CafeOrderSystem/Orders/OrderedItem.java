package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.Menu.Items.CustomItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents an order line item containing a {@link OrderItem} and its quantity.
 * <p>
 * Used internally within {@link CustomerOrder} to track quantities of ordered menu items
 * and support functionality such as quantity adjustments and item customization.
 * This class supports JSON deserialization and gracefully ignores unknown fields.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderedItem {
    private OrderItem item;
    private int quantity;

    /**
     * Default constructor required for JSON deserialization
     */
    public OrderedItem() {}

    /**
     * Constructs a new {@code OrderedItem} with the specified menu item and quantity.
     *
     * @param item     the ordered menu item
     * @param quantity the quantity of the item ordered
     */
    public OrderedItem(OrderItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Increments the quantity of this item by one
     */
    public void increaseQuantity() {
        this.quantity++;
    }

    /**
     * Return the {@link OrderItem} item
     */
    public OrderItem getItem() {
        return item;
    }

    /**
     * Get the amount of {@link OrderItem} inside this item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Checks whether the item ID of the contained {@link OrderItem}
     * matches the given ID.
     *
     * @param itemID the item ID to check
     * @return {@code true} if the contained item matches the ID; {@code false} otherwise
     */
    public boolean existID(String itemID){
        return item.getItemID().equals(itemID);
    }

    /**
     * Decrements the quantity of the item by one
     */
    public void decreaseQuantity(){
        this.quantity--;
    }

    /**
     * Checks if this ordered item contains the given menu item
     * (i.e., they are equal according to {@code equals()}).
     *
     * @param orderItem the item to compare against
     * @return {@code true} if the items match; {@code false} otherwise
     */
    public boolean contain(OrderItem orderItem){
        return item.equals(orderItem);
    }

    /**
     * Applies customization to the underlying item.
     * This delegates to {@link OrderItem#modifyOrderItem(CustomItem)}.
     *
     * @param customItem the customization to apply
     */
    public void modifyItem(CustomItem customItem){
        item.modifyOrderItem(customItem);
    }

    @Override
    public String toString() {
       return "Order Item: " + item.toString() + " Quantity: " + quantity;
    }
}
