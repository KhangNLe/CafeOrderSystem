package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.Exceptions.CafeSystemException;
import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;
import Cafe.CafeOrderSystem.Menu.Items.*;

import java.time.LocalDate;
import java.util.UUID;

public class OrdersManagement {
    private final CafeOrders orders;
    private static OrdersManagement ordersManagement;

    public OrdersManagement(){
        orders = CafeOrders.getInstance();
    }

    public String createNewOrder(){
        String orderId = LocalDate.now() + "-" + UUID.randomUUID();
        CustomerOrder order = CustomerOrder.newEmptyOrder(orderId);
        orders.putInPendingOrder(order);

        return orderId;
    }

    public CustomerOrder getNextOrder(){
        return orders.getNextPendingOrder();
    }

    public void completeOrder(String orderID){
        orders.completeOrder(orderID);
    }

    public void addItemIntoOrder(String orderID, OrderItem orderItem){
        CustomerOrder order = orders.lookUpPendingOrder(orderID);
        verifyCustomerOrder(order, orderID);
        order.addOrderItem(orderItem);
    }

    public void modifyOrderItem(String orderID, CustomItem customItem, String itemID) {

        CustomerOrder order = orders.lookUpPendingOrder(orderID);
        verifyCustomerOrder(order, orderID);
        order.customizeOrderItem(itemID, customItem);
    }

    public void removeItemFromOrder(String orderID, OrderItem orderItem){
        CustomerOrder order = orders.lookUpPendingOrder(orderID);
        verifyCustomerOrder(order, orderID);
        order.removeOrderItem(orderItem);
    }

    private void verifyCustomerOrder(CustomerOrder order, String orderID){
        if (order == null){
            throw new InvalidInputException(
                    String.format("Order with id %s does not exist", orderID)
            );
        }
    }
}
