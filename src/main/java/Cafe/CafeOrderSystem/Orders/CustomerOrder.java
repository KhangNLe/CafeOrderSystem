package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.Exceptions.*;
import Cafe.CafeOrderSystem.Menu.Items.CustomItem;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a customer's order in the cafe system.
 * <p>
 * An order is identified by a unique ID and contains metadata such as the order date,
 * total price, and current {@link OrderStatus}. Items in the order are managed as a list
 * of {@link OrderedItem}, which supports quantity aggregation and optional customization
 * via {@link CustomItem}.
 * <p>
 * This class also provides behavior for managing the lifecycle of an order:
 * adding/removing items, modifying items, tracking total cost, and updating status.
 * It supports deserialization with Jackson and gracefully handles partial input.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerOrder {
    private final String orderDate;
    private final String orderID;
    private final List<OrderedItem> orderItems;
    private OrderStatus orderStatus;
    private double totalPrice;

    /**
     * Factory method for creating a new, empty {@code CustomerOrder} instance with a given ID.
     *
     * @param orderID the unique ID for the order
     * @return a new {@code CustomerOrder} with the current timestamp, empty item list,
     *         pending status, and zero total price
     */
    public static CustomerOrder newEmptyOrder(String orderID) {
        return new CustomerOrder(
                LocalDateTime.now().toString(),
                orderID,
                new ArrayList<>(),
                OrderStatus.PENDING,
                0.0
        );
    }

    /**
     * Constructs a new {@code CustomerOrder} from serialized input.
     * Handles {@code null} values for optional fields by providing sensible defaults.
     *
     * @param orderDate    the date/time the order was created
     * @param orderID      the unique ID for the order
     * @param orderedItems the list of items in the order
     * @param orderStatus  the current status of the order
     * @param totalPrice   the current total cost of the order
     */
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

    /**
     * Adds an item to the order. If the item already exists in the order,
     * its quantity is incremented instead of adding a duplicate.
     * Also updates the total price.
     *
     * @param orderItem the item to be added
     */
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

    /**
     * Removes one instance of the specified item from the order.
     * If the quantity reaches zero, the item is removed entirely.
     *
     * @param orderItem the item to remove
     */
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

    /**
     * Applies a custom modification to a specific item in the order by item ID.
     * Increases the total order price by the cost of the custom item.
     *
     * @param itemID     the ID of the item to modify
     * @param customItem the custom addition to apply
     * @throws InvalidModifyingException if the item is not found in the order
     */
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

    /**
     * Updates the order's status to the given value.
     *
     * @param orderStatus the new status to apply
     * @throws InvalidModifyingException if the transition is invalid
     */
    public void changeOrderStatus(OrderStatus orderStatus) {
        if (orderStatus.changeStatusAttempt(orderStatus)) {
            this.orderStatus = orderStatus;
        } else {
            throw new InvalidModifyingException(
                    String.format("Cannot change status from %s to %s", this.orderStatus, orderStatus
            ));
        }
    }

    /**
     * Returns the ordered item with the given ID, or {@code null} if not found.
     *
     * @param itemID the ID of the item to retrieve
     * @return the matching {@link OrderedItem}, or {@code null} if not present
     * @throws InvalidInputException if the ID is invalid
     */
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


    // Trevor: I added this so I could do some formatting for the retreival of the order on the back end
    public String shortSummary() {
        return String.format("Order #%s | %d item(s) | $%.2f | %s",
            orderID,
            orderItems.size(),
            totalPrice,
            orderStatus
        );
    }
}
