package Cafe.CafeOrderSystem.Orders;

import java.util.*;

public class CafeOrders {
    private final Queue<CustomerOrder> pendingOrders;
    private final List<CustomerOrder> fulfilledOrders;
    private static CafeOrders instance;

    private CafeOrders() {
        pendingOrders = new LinkedList<>();
        fulfilledOrders = new ArrayList<>();
    }

    public static CafeOrders getInstance() {
        if (instance == null) {
            instance = new CafeOrders();
        }
        return instance;
    }

    public CustomerOrder getNextPendingOrder() {
        if (!pendingOrders.isEmpty()) {
            return pendingOrders.poll();
        }

        return null;
    }

    public void completeOrder(String orderID) {
        CustomerOrder order = lookUpPendingOrder(orderID);
        if (order != null) {
            pendingOrders.add(order);
        }
        throw new IllegalArgumentException(
                String.format("There no pending order with ID %s", orderID)
        );
    }

    public void putInPendingOrder(CustomerOrder order) {
        if (pendingOrders.contains(order)) {
            throw new IllegalArgumentException(
                    String.format("Order id %s is already in queue", order.getOrderID()
            ));
        }
        pendingOrders.add(order);
    }

    public CustomerOrder lookUpPendingOrder(String orderID) {
        for (CustomerOrder order : pendingOrders) {
            if (order.getOrderID().equals(orderID)) {
                return order;
            }
        }
        return null;
    }

    public List<CustomerOrder> getTodayOrders(){
        return fulfilledOrders;
    }

    public List<CustomerOrder> getPendingOrders() {
        return List.copyOf(pendingOrders);
    }

}
