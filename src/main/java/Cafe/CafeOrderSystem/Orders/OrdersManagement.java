package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;
import Cafe.CafeOrderSystem.Exceptions.InvalidModifyingException;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientList;
import Cafe.CafeOrderSystem.Inventory.Inventory;
import Cafe.CafeOrderSystem.JsonParser.OrderItem.CustomerOrderParser;
import Cafe.CafeOrderSystem.Menu.Items.*;
import Cafe.CafeOrderSystem.CatalogItems.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Provides high-level operations for managing customer orders in a cafe system.
 * <p>
 * This class acts as a service layer that delegates order lifecycle tasks
 * (creation, modification, finalization, and fulfillment) to {@link CafeOrders}.
 * It abstracts order handling logic and ensures correctness through validation
 * before delegating to lower-level classes.
 * </p>
 */
public class OrdersManagement {
    private final CafeOrders orders;
    private final Inventory cafeInventory;

    /**
     * Constructs an {@code OrdersManagement} instance with a given {@code CafeOrders} manager.
     *
     * @param orders the underlying order system managing active, pending, and fulfilled orders
     */
    public OrdersManagement(CafeOrders orders, Inventory cafeInventory) {
        this.orders = orders;
        this.cafeInventory = cafeInventory;
    }

    /**
     * Creates and starts a new, empty customer order.
     * <p>
     * The generated order ID is based on the current date and a random UUID.
     * </p>
     *
     * @return the unique identifier of the newly created order
     */
    public String createNewOrder(String customerName) {
        String orderId = LocalDate.now() + "-" + UUID.randomUUID();
        CustomerOrder order = CustomerOrder.newEmptyOrder(orderId, customerName);
        orders.startOrder(order);

        return orderId;
    }

    /**
     * Retrieves the list of all pending orders that are awaiting fulfillment.
     *
     * @return a list of pending {@link CustomerOrder}s
     */
    public List<CustomerOrder> getPendingOrder() {
        return orders.getPendingOrders();
    }

    /**
     * Retrieves the list of completed and fulfilled orders.
     *
     * @return a list of fulfilled {@link CustomerOrder}s representing order history
     */
    public List<CustomerOrder> getOrderHistory() {
        return orders.getOrderHistory();
    }

    /**
     * Moves an active order to the pending state, marking it as ready for fulfillment.
     *
     * @param orderID the ID of the order to finalize
     */
    public void finalizeActiveOrder(String orderID) {
        orders.finalizeActiveOrder(orderID);
    }

    /**
     * Retrieves and removes the next order in the pending queue.
     *
     * @return the next pending {@link CustomerOrder}, or {@code null} if the queue is empty
     */
    public CustomerOrder getNextOrder() {
        return orders.getNextPendingOrder();
    }

    /**
     * Marks an order as fulfilled by adding it to the fulfillment queue.
     *
     * @param order the order to mark as fulfilled
     */
    public void fulfilledOrder(CustomerOrder order) {
        orders.fulfilledOrder(order);
    }

    /**
     * Adds a new item to the specified active order.
     *
     * @param orderID   the ID of the order to update
     * @param orderItem the item to add
     */
    public void addItemIntoOrder(String orderID, OrderItem orderItem) {
        CustomerOrder order = orders.lookUpActiveOrder(orderID);
        verifyCustomerOrder(order, orderID);
        order.addOrderItem(orderItem);
    }

    /**
     * Applies customization to an existing item in an order.
     *
     * @param orderID    the ID of the order to modify
     * @param customItem the customization to apply
     * @param itemID     the ID of the item to customize
     */
    public void modifyOrderItem(String orderID, CustomItem customItem, String itemID) {

        CustomerOrder order = orders.lookUpActiveOrder(orderID);
        verifyCustomerOrder(order, orderID);
        order.customizeOrderItem(itemID, customItem);
    }

    /**
     * Removes an item from the specified active order.
     *
     * @param orderID   the ID of the order to update
     * @param orderItem the item to remove
     */
    public void removeItemFromOrder(String orderID, OrderItem orderItem) {
        CustomerOrder order = orders.lookUpActiveOrder(orderID);
        verifyCustomerOrder(order, orderID);
        order.removeOrderItem(orderItem);
    }

    /**
     * Creates a beverage order item based on the selected size and optional customizations.
     *
     * @param item   the base beverage menu item
     * @param size   the desired beverage size
     * @param addOns optional customization (can be {@code null})
     * @return the constructed {@link OrderItem}
     */
    public OrderItem createBeverageItem(BeverageItem item, BeverageSize size,
                                        List<CustomItem> addOns) {
        BeverageItem beverage = item.copyOf();
        BeverageCost cost = beverage.cost().get(size);
        OrderItem orderItem = createOrderItem(beverage.id(), beverage.name(), beverage.type(),
                cost.ingredients(), cost.price());

        if (addOns != null && !addOns.isEmpty()) {
            addOns.forEach(orderItem::modifyOrderItem);
        }

        checkForAvailableIngredients(orderItem);
        return orderItem;
    }

    /**
     * Creates a pastries order item based on the original item's cost and ingredients.
     *
     * @param item the base pastries menu item
     * @return the constructed {@link OrderItem}
     */
    public OrderItem createPastriesItem(PastriesItem item) {
        PastriesItem pastries = item.copyOf();
        PastriesCost cost = pastries.cost();
        OrderItem orderItem = createOrderItem(pastries.id(), pastries.name(), pastries.type(),
                cost.ingredients(), cost.price());

        checkForAvailableIngredients(orderItem);
        return orderItem;
    }

    /**
     * Safety check for if {@link OrderItem}s are created but Customer canceled the order.
     * All the required ingredients will be returned to the inventory.
     *
     * @param items the List of {@code OrderItem} from a {@link CustomerOrder}
     */
    public void returnIngredientsToInventory(List<OrderItem> items) {
        items.forEach(this::returnIngredients);
    }

    /**
     * Retuned the ingredient requirement from one {@link OrderItem} back into inventory
     *
     * @param item The {@code OrderItem} to get the ingredient to be returned.
     */
    private void returnIngredients(OrderItem item) {
        Map<Ingredients, Integer> ingredients = item.getIngredientsCost();
        ingredients.forEach(cafeInventory::modifyInventory);
    }

    /**
     * Validates that a given order exists. Throws an exception if it's {@code null}.
     *
     * @param order   the order to check
     * @param orderID the order ID for error messaging
     * @throws InvalidInputException if the order is {@code null}
     */
    private void verifyCustomerOrder(CustomerOrder order, String orderID) {
        if (order == null) {
            throw new InvalidInputException(
                    String.format("Order with id %s does not exist", orderID)
            );
        }
    }

    /**
     * Factory method to construct a new {@link OrderItem}.
     *
     * @param id    the item ID
     * @param name  the item name
     * @param type  the item type (e.g., beverage, pastry)
     * @param cost  the map of ingredient costs
     * @param price the item's price
     * @return a new {@link OrderItem}
     */
    private OrderItem createOrderItem(String id, String name, MenuType type, Map<Ingredients,
            Integer> cost, double price) {
        return new OrderItem(id, name, type, cost, price);
    }

    /**
     * Validates that an order is not null and contains at least one item.
     * <p>
     * Throws an {@link InvalidInputException} if either condition fails.
     * </p>
     *
     * @param order the order to validate
     */
    private void validateCustomerOrder(CustomerOrder order) {
        if (order == null) {
            throw new InvalidInputException("The given order is null");
        }

        if (order.getOrderItems().isEmpty()) {
            throw new InvalidInputException(
                    String.format("Cannot add order %s into the pending orders", order)
            );
        }
    }

    /**
     * Validate and check if there is enough {@link Ingredients} stocked inside {@link Inventory}
     * in order to produce an {@link OrderItem}
     *
     * @param orderItem The {@code OrderItem} that will be checked for ingredient availability to
     *                  produce
     */
    private void checkForAvailableIngredients(OrderItem orderItem) {
        Map<Ingredients, Integer> ingredients = orderItem.getIngredientsCost();

        ingredients.forEach((ingredient, amount) -> {
            if (!cafeInventory.modifyInventory(ingredient, -amount)) {
                throw new InvalidInputException(
                        String.format("Not enough %s for the %s",
                                ingredient.getName(), orderItem.getItemName())
                );
            }
        });
    }

    // Trevor: I had to add this so I could reflect front end chnages on the back end
    public boolean updateOrderStatus(String orderID, OrderStatus newStatus) {
        try {
            List<CustomerOrder> pending = orders.getPendingOrders();
            Iterator<CustomerOrder> pendingIterator = pending.iterator();
            while (pendingIterator.hasNext()) {
                CustomerOrder order = pendingIterator.next();
                if (order.getOrderID().equals(orderID)) {
                    order.changeOrderStatus(newStatus);
                    pendingIterator.remove();

                    if (newStatus == OrderStatus.IN_PROCESS) {
                        orders.startOrder(order); // Move to active orders
                    } else if (newStatus == OrderStatus.READY) {
                        orders.fulfilledOrder(order); // Move to fulfilled orders
                    }
                    return true;
                }
            }

            try {
                CustomerOrder activeOrder = orders.lookUpActiveOrder(orderID);
                if (activeOrder != null) {
                    activeOrder.changeOrderStatus(newStatus);
                    if (newStatus == OrderStatus.READY) {
                        orders.fulfilledOrder(activeOrder);
                    }
                    return true;
                }
            } catch (InvalidInputException e) {
                // Order not found in active orders - continue checking
            }

            return false; // Order not found
        } catch (InvalidModifyingException e) {
            return false;
        }
    }
}
