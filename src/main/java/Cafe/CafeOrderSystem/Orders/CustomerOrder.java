package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.Exceptions.*;
import Cafe.CafeOrderSystem.Menu.Items.CustomItem;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerOrder {
    private final String orderDate;
    private final String orderID;
    private final List<OrderedItem> orderItems;
    private OrderStatus orderStatus;
    private double totalPrice;

    public static CustomerOrder newEmptyOrder(String orderID) {
        return new CustomerOrder(
                LocalDateTime.now().toString(),
                orderID,
                new ArrayList<>(),
                OrderStatus.PENDING,
                0.0
        );
    }

    @JsonCreator
    public CustomerOrder(
            @JsonProperty("orderDate") String orderDate,
            @JsonProperty("orderID") String orderID,
            @JsonProperty("orderItems") List<OrderedItem> orderedItems,
            @JsonProperty("orderStatus") OrderStatus orderStatus,
            @JsonProperty("totalPrice") double totalPrice
    ){
        this.orderDate = (orderDate == null)? LocalDateTime.now().toString() : orderDate;
        this.orderID = orderID;
        this.orderItems = (orderedItems == null)? new ArrayList<>(): orderedItems;
        this.orderStatus = (orderStatus == null)? OrderStatus.PENDING : orderStatus;
        this.totalPrice = totalPrice;
    }

    public void addOrderItem(OrderItem orderItem) {
        boolean isNew = true;
        for (OrderedItem item : orderItems) {
            if (item.getItem().equals(orderItem)){
                item.increaseQuantity();
                isNew = false;
            }
        }

        if (isNew) {
            orderItems.add(new OrderedItem(orderItem, 1));
        }

        totalPrice += orderItem.getPrice();
    }

    public void removeOrderItem(OrderItem orderItem){
        for (OrderedItem item : orderItems) {
            if (item.contain(orderItem)){
                item.decreaseQuantity();

                if (item.getQuantity() <= 0){
                    orderItems.remove(item);
                }
            }
        }
    }

    public void customizeOrderItem(String itemID, CustomItem customItem) {
        OrderedItem item = getOrderItem(itemID);
        if (item != null) {
            item.modifyItem(customItem);
            totalPrice += customItem.additionalPrice();
        } else {
            throw new InvalidModifyingException(
                    String.format("Item with id %s not found inside order id %s",
                            itemID, orderID)
            );
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (orderStatus.changeStatusAttempt(orderStatus)) {
            this.orderStatus = orderStatus;
        } else {
            throw new InvalidModifyingException(
                    String.format("Cannot change status from %s to %s", this.orderStatus, orderStatus
            ));
        }
    }

    private OrderedItem getOrderItem(String itemID) {
        if (itemID == null || itemID.isEmpty()) {
            throw new InvalidInputException("ItemID cannot be null or empty");
        }

        for (OrderedItem orderedItem : orderItems) {
            if (orderedItem.existID(itemID)) {
                return orderedItem;
            }
        }

        return null;
    }

    public List<OrderedItem> getOrderItems() {
        return orderItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getOrderID(){
        return orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CustomerOrder{").append('\n');
        sb.append("orderDate='").append(orderDate).append(", ");
        sb.append("orderID='").append(orderID).append(", ");
        for (OrderedItem orderedItem : orderItems) {
            sb.append("orderItem=").append(orderedItem).append(", ");
        }
        sb.append("orderStatus=").append(orderStatus).append(", ");
        sb.append("totalPrice=").append(totalPrice);
        sb.append("}").append('\n');
        return sb.toString();
    }
}
