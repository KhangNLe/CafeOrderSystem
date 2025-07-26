package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.Inventory.*;
import Cafe.CafeOrderSystem.Menu.CafeMenu;

import java.time.LocalDate;
import java.util.UUID;

public class OrdersManagement {
    private final CafeMenu cafeMenu;
    private final CafeOrders orders;
    private static OrdersManagement ordersManagement;

    private OrdersManagement(){
        cafeMenu = CafeMenu.getInstance();
        orders = CafeOrders.getInstance();
    }

    public static OrdersManagement getInstance(){
        if (ordersManagement == null){
            ordersManagement = new OrdersManagement();
        }
        return ordersManagement;
    }

    public CustomerOrder getCustomerOrder(String customerId){
        return orders.getActiveOrder(customerId);
    }

    public String createOrder(){
        String orderId = LocalDate.now() + "-" + UUID.randomUUID();
        CustomerOrder order = CustomerOrder.newEmptyOrder(orderId);
        orders.putInPendingOrder(order);
        return orderId;
    }

}
