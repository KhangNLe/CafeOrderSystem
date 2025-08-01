package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.Menu.Items.CustomItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderedItem {
    private OrderItem item;
    private int quantity;

    public OrderedItem() {}

    public OrderedItem(OrderItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public OrderItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean existID(String itemID){
        return item.getItemID().equals(itemID);
    }

    public void decreaseQuantity(){
        this.quantity--;
    }

    public boolean contain(OrderItem orderItem){
        return item.equals(orderItem);
    }

    public void modifyItem(CustomItem customItem){
        item.modifyOrderItem(customItem);
    }

    @Override
    public String toString() {
       return "Order Item: " + item.toString() + " Quantity: " + quantity;
    }
}
