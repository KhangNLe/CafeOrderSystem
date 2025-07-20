package Cafe.CafeOrderSystem.JsonParser.OrderItem;

import Cafe.CafeOrderSystem.Orders.CustomerOrder;

public abstract class OrdersParser {
    protected static void validateCustomerOrder(CustomerOrder order, String filePath){
        if (order.getOrderID() == null || order.getOrderID().isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Order ID is null or empty in customer order: %s in %s",
                            order, filePath
                    ));
        }

        if (order.getOrderItems().isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Order number %s inside %s does not contain any item data",
                            order.getOrderID(), filePath
                    ));
        }

        if (order.getTotalPrice() <= 0){
            throw new IllegalArgumentException(
                    String.format("Order id %s from %s has an incorrect total price of %.2f",
                            order.getOrderID(), filePath, order.getTotalPrice()
                    ));
        }
    }
}
