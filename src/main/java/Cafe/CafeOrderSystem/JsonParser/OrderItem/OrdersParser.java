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

    @Override
    public void endCollection(){
        pendingOrders.endCollection();
        ordersHistory.endCollection();
    }

    public CustomerOrderParser getPendingOrdersParser(){
        return (CustomerOrderParser) pendingOrders;
    }

    public CustomerOrderParser getOrdersHistoryParser(){
        return (CustomerOrderParser) ordersHistory;
    }
}
