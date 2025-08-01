package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;
import Cafe.CafeOrderSystem.JsonParser.OrderItem.CustomerOrderParser;

import java.util.*;

/**
 * Manages the lifecycle of customer orders in the cafe system,
 * including active orders currently being processed, pending orders queued for preparation,
 * and fulfilled orders representing completed transactions.
 * <p>
 * Active orders are tracked in-memory using a map keyed by order ID,
 * while pending and fulfilled orders are managed by {@link CustomerOrderParser} collections,
 * which typically back persistent storage.
 */
public class CafeOrders {
    private final Map<String, CustomerOrder> ordering;
    private final CustomerOrderParser pendingOrders;
    private final CustomerOrderParser fulfilledOrders;

    /**
     * Creates a new CafeOrders manager using the specified parsers for
     * pending and fulfilled orders.
     *
     * @param pendingOrders the parser managing pending orders
     * @param fulfilledOrders the parser managing fulfilled orders
     */
    public CafeOrders(CustomerOrderParser pendingOrders, CustomerOrderParser fulfilledOrders) {
        ordering = new HashMap<>();
        this.pendingOrders = pendingOrders;
        this.fulfilledOrders = fulfilledOrders;
    }

    /**
     * Retrieves and removes the next pending order from the pending orders queue.
     *
     * @return the next {@link CustomerOrder} to process, or {@code null} if no pending orders exist
     */
    public CustomerOrder getNextPendingOrder() {
        if (!pendingOrders.getCollection().isEmpty()) {
            return pendingOrders.getCollection().removeFirst();
        }

        return null;
    }

    /**
     * Starts tracking a new active order.
     *
     * @param order the {@link CustomerOrder} to track as active
     */
    public void startOrder(CustomerOrder order) {
        ordering.put(order.getOrderID(), order);
    }

    /**
     * Finalizes an active order by removing it from active tracking and
     * re-adding it to the pending orders queue.
     *
     * @param orderID the ID of the order to finalize
     */
    public void finalizeActiveOrder(String orderID){
        CustomerOrder order = ordering.remove(orderID);
        pendingOrders.getCollection().addLast(order);
    }

    /**
     * Adds a fulfilled order to the collection of completed orders.
     *
     * @param order the {@link CustomerOrder} that has been fulfilled
     */
    public void fulfilledOrder(CustomerOrder order) {
        fulfilledOrders.getCollection().addLast(order);
    }

    /**
     * Looks up an active order by its unique ID.
     *
     * @param orderID the unique ID of the active order
     * @return the matching {@link CustomerOrder}
     * @throws InvalidInputException if no active order exists with the given ID
     */
    public CustomerOrder lookUpActiveOrder(String orderID) {
        CustomerOrder order =  ordering.get(orderID);
        if (order == null) {
            throw new InvalidInputException(
                    String.format("Order with ID %s does not exist", orderID)
            );
        }
        return order;
    }

    /**
     * Returns the list of fulfilled (completed) orders.
     *
     * @return the list of fulfilled {@link CustomerOrder} objects
     */
    public List<CustomerOrder> getOrderHistory(){
        return fulfilledOrders.getCollection();
    }

    /**
     * Returns an immutable copy of the list of pending orders.
     *
     * @return an unmodifiable {@link List} of pending {@link CustomerOrder} objects
     */
    public List<CustomerOrder> getPendingOrders() {
        return pendingOrders.getCollection();
    }
}
