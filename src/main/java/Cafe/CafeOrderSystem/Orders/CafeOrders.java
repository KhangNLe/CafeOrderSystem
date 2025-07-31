package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.JsonParser.OrderItem.OrderHistoryParser;
import java.util.*;

public class CafeOrders {
    private final Queue<CustomerOrder> pendingOrders;
    private final Map<String, CustomerOrder> activeOrders;
    private final List<CustomerOrder> fulfilledOrders;
    private static CafeOrders instance;

    private CafeOrders() {
        pendingOrders = new LinkedList<>();
        fulfilledOrders = new ArrayList<>();
        activeOrders = new HashMap<>();
    }

    public static CafeOrders getInstance() {
        if (instance == null) {
            instance = new CafeOrders();
        }
        return instance;
    }

    public CustomerOrder getNextPendingOrder() {
        return pendingOrders.poll();
    }

    public void completeOrder(CustomerOrder order) {
        OrderHistoryParser.addOrderToHistory(order);
        fulfilledOrders.add(order);
    }

    public void putInPendingOrder(CustomerOrder order) {
        if (pendingOrders.contains(order)) {
            throw new IllegalArgumentException(
                    String.format("Order id %s is already in queue", order.getOrderID()
            ));
        }
        pendingOrders.add(order);
        activeOrders.put(order.getOrderID(), order);
    }

    public CustomerOrder getActiveOrder(String orderID) {
        if (!activeOrders.containsKey(orderID)) {
            throw new IllegalArgumentException(
                    String.format("Order id %s not found on current pending orders", orderID)
            );
        }
        return activeOrders.get(orderID);
    }

    public List<CustomerOrder> getTodayOrders(){
        return fulfilledOrders;
    }

    public List<CustomerOrder> getPendingOrders() {
        return List.copyOf(pendingOrders);
    }

    public void clearPending() {
    pendingOrders.clear();
}
    

}
