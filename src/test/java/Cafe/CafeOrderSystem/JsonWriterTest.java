package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import Cafe.CafeOrderSystem.Orders.CafeOrders;
import Cafe.CafeOrderSystem.Orders.OrderItem;
import Cafe.CafeOrderSystem.Orders.OrdersManagement;
import org.junit.jupiter.api.*;
import Cafe.CafeOrderSystem.JsonParser.OrderItem.*;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.logging.Logger;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonWriterTest {
    private static Logger LOGGER = Logger.getLogger(JsonWriterTest.class.getName());

    @Test
    @Order(2)
    @DisplayName("Test for putting data inside Json file")
    public void testJsonWriter() {
        CafeOrders orders = CafeOrders.getInstance();
        CustomerOrder order1 = CustomerOrder.newEmptyOrder("abc");
        Map<Ingredients, Integer> ingredientsCost = new HashMap<>();
        ingredientsCost.put(Ingredients.MILK, 3);
        ingredientsCost.put(Ingredients.SUGAR, 4);
        ingredientsCost.put(Ingredients.FLOUR, 1000);
        OrderItem item1 = new OrderItem("123", "stuffs", MenuType.COFFEE, ingredientsCost, 10);
        OrderItem item2 = new OrderItem("456", "other stuffs", MenuType.COFFEE, ingredientsCost, 10);

        order1.addOrderItem(item1);
        order1.addOrderItem(item2);

        orders.putInPendingOrder(order1);

        CustomerOrder order2 = CustomerOrder.newEmptyOrder("def");
        order2.addOrderItem(item1);
        order2.addOrderItem(item1);
        order2.addOrderItem(item1);

        orders.putInPendingOrder(order2);

        PendingOrderParser.savePendingOrders();
    }

    @Test
    @Order(3)
    @DisplayName("Test for adding order into order history")
    public void testOrderHistory() {
        CustomerOrder order1 = CustomerOrder.newEmptyOrder("abc");
        Map<Ingredients, Integer> ingredientsCost = new HashMap<>();
        ingredientsCost.put(Ingredients.MILK, 3);
        ingredientsCost.put(Ingredients.SUGAR, 4);
        ingredientsCost.put(Ingredients.FLOUR, 1000);

        OrderItem item1 = new OrderItem("123", "stuffs", MenuType.COFFEE, ingredientsCost, 10);
        OrderItem item2 = new OrderItem("456", "other stuffs", MenuType.COFFEE, ingredientsCost, 10);

        order1.addOrderItem(item1);
        order1.addOrderItem(item2);
        order1.addOrderItem(item1);
        order1.addOrderItem(item1);
        order1.addOrderItem(item2);

        OrderHistoryParser.addOrderToHistory(order1);
    }

    @Test
    @Order(4)
    @DisplayName("Get Order History")
    public void testGetOrderHistory() {
        List<CustomerOrder> ordersHistory = OrderHistoryParser.loadOrderHistory();

        assertNotNull(ordersHistory);
        assertFalse(ordersHistory.isEmpty());
        LOGGER.info(ordersHistory.toString());
    }

    @Test
    @Order(5)
    @DisplayName("Get Pending order")
    public void testGetPendingOrder() {
        CafeOrders orders = CafeOrders.getInstance();
        PendingOrderParser.getPendingOrders();

        assertNotNull(orders.getPendingOrders());
        assertFalse(orders.getPendingOrders().isEmpty());
        LOGGER.info(orders.getPendingOrders().toString());
    }

    @Test
    @Order(1)
    @DisplayName("Test for saving empty pending order list")
    void testForEmptyOrderList(){
        OrdersManagement management = OrdersManagement.getInstance();

        PendingOrderParser.savePendingOrders();
        PendingOrderParser.getPendingOrders();

        CafeOrders orders = CafeOrders.getInstance();
        assertNotNull(orders.getPendingOrders());
        assertTrue(orders.getPendingOrders().isEmpty());

    }
}
