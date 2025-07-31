package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;
import Cafe.CafeOrderSystem.JsonParser.OrderItem.CustomerOrderParser;

import java.util.*;

public class CafeOrders {
    private final Map<String, CustomerOrder> ordering;
    private final CustomerOrderParser pendingOrders;
    private final CustomerOrderParser fulfilledOrders;
    private static CafeOrders instance;

    public CafeOrders(CustomerOrderParser pendingOrders, CustomerOrderParser fulfilledOrders) {
        ordering = new HashMap<>();
        this.pendingOrders = pendingOrders;
        this.fulfilledOrders = fulfilledOrders;
    }

    public CustomerOrder getNextPendingOrder() {
        if (!pendingOrders.getCollection().isEmpty()) {
            return pendingOrders.getCollection().getFirst();
        }

        return null;
    }

    public void startOrder(CustomerOrder order) {
        ordering.put(order.getOrderID(), order);
    }

    public void finalizeActiveOrder(String orderID){
        CustomerOrder order = ordering.remove(orderID);
        pendingOrders.getCollection().addLast(order);
    }


    public void fulfilledOrder(CustomerOrder order) {
        fulfilledOrders.getCollection().addLast(order);
    }

    public CustomerOrder lookUpActiveOrder(String orderID) {
        CustomerOrder order =  ordering.get(orderID);
        if (order == null) {
            throw new InvalidInputException(
                    String.format("Order with ID %s does not exist", orderID)
            );
        }
        return order;
    }

    public List<CustomerOrder> getTodayOrders(){
        return fulfilledOrders.getCollection();
    }

    public List<CustomerOrder> getPendingOrders() {
        return List.copyOf(pendingOrders.getCollection());
    }

}
