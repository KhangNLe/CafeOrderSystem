package Cafe.CafeOrderSystem.Orders;

import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;
import Cafe.CafeOrderSystem.JsonParser.OrderItem.CustomerOrderParser;
import Cafe.CafeOrderSystem.Menu.Items.*;
import Cafe.CafeOrderSystem.CatalogItems.*;

import java.time.LocalDate;
import java.util.UUID;
import java.util.*;

public class OrdersManagement {
    private final CafeOrders orders;
    private static OrdersManagement ordersManagement;

    public OrdersManagement(CafeOrders orders) {
        this.orders = orders;
    }

    public String createNewOrder(){
        String orderId = LocalDate.now() + "-" + UUID.randomUUID();
        CustomerOrder order = CustomerOrder.newEmptyOrder(orderId);
        orders.startOrder(order);

        return orderId;
    }

    public void finalizeActiveOrder(String orderID){
        orders.finalizeActiveOrder(orderID);
    }

    public CustomerOrder getNextOrder(){
        return orders.getNextPendingOrder();
    }

    public void fulfilledOrder(CustomerOrder order){
        orders.fulfilledOrder(order);
    }

    public void addItemIntoOrder(String orderID, OrderItem orderItem){
        CustomerOrder order = orders.lookUpActiveOrder(orderID);
        verifyCustomerOrder(order, orderID);
        order.addOrderItem(orderItem);
    }

    public void modifyOrderItem(String orderID, CustomItem customItem, String itemID) {

        CustomerOrder order = orders.lookUpActiveOrder(orderID);
        verifyCustomerOrder(order, orderID);
        order.customizeOrderItem(itemID, customItem);
    }

    public void removeItemFromOrder(String orderID, OrderItem orderItem){
        CustomerOrder order = orders.lookUpActiveOrder(orderID);
        verifyCustomerOrder(order, orderID);
        order.removeOrderItem(orderItem);
    }

    public OrderItem createBeverageItem(BeverageItem beverage, BeverageSize size, CustomItem addOn){
        BeverageCost cost = beverage.cost().get(size);
        OrderItem item = createOrderItem(beverage.id(), beverage.name(), beverage.type(),
                cost.ingredients(), cost.price());

        if (addOn != null){
            item.modifyOrderItem(addOn);
        }
        return item;
    }

    public OrderItem createPastriesItem(PastriesItem pastries){
        PastriesCost cost = pastries.cost();
        return createOrderItem(pastries.id(), pastries.name(), pastries.type(),
                cost.ingredients(), cost.price());
    }

    private void verifyCustomerOrder(CustomerOrder order, String orderID){
        if (order == null){
            throw new InvalidInputException(
                    String.format("Order with id %s does not exist", orderID)
            );
        }
    }

    private OrderItem createOrderItem(String id, String name, MenuType type, Map<Ingredients,
            Integer> cost, double price){
        return new OrderItem(id, name, type, cost, price);
    }
}
