package Cafe.CafeOrderSystem.JsonParser.OrderItem;

import Cafe.CafeOrderSystem.JsonParser.Parsers;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;

public class OrdersParser implements Parsers{
    private final Parsers pendingOrders;
    private final Parsers ordersHistory;

    public OrdersParser(Parsers pendingOrders, Parsers ordersHistory) {
        this.pendingOrders = pendingOrders;
        this.ordersHistory = ordersHistory;
    }

    @Override
    public void startCollection(){
        pendingOrders.startCollection();
        ordersHistory.startCollection();
    }

    public void completeOrder(CustomerOrder order){
        ((CustomerOrderParser) ordersHistory).completeOrder(order);
    }

    @Override
    public void endCollection(){
        pendingOrders.endCollection();
        ordersHistory.endCollection();
    }
}
