package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
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
    private static final Logger LOGGER = Logger.getLogger(JsonWriterTest.class.getName());
    private static Cafe cafeShop;
    private static List<BeverageItem> beverages;
    private static List<PastriesItem> pastries;
    private static OrdersManagement ordersManagement;

    @BeforeAll
    static void beforeAll() {
        cafeShop = new Cafe();
        cafeShop.startShop();
        beverages = cafeShop.getCafeMenuManagement().getBeverageItems();
        pastries = cafeShop.getCafeMenuManagement().getPastriesItems();
        ordersManagement = cafeShop.getOrdersManagement();
    }

    @Test
    @Order(1)
    @DisplayName("Test writing pending orders")
    void testWritingPendingOrders() {
        BeverageItem bItem = beverages.get(3);
        PastriesItem pItem = pastries.get(2);

        String orderID = ordersManagement.createNewOrder();

        OrderItem orderItem = ordersManagement.createBeverageItem(bItem,
                new BeverageSize("small"), null);

        OrderItem orderItem2 = ordersManagement.createPastriesItem(pItem);

        assertNotNull(orderItem);
        assertNotNull(orderItem2);

        ordersManagement.addItemIntoOrder(orderID, orderItem);
        ordersManagement.addItemIntoOrder(orderID, orderItem2);

        ordersManagement.finalizeActiveOrder(orderID);

    }

    @Test
    @Order(2)
    @DisplayName("Test fulfill an order")
    void testOrderFulfill(){
        PastriesItem pItem = pastries.get(2);

        String orderID = ordersManagement.createNewOrder();

        OrderItem orderItem = ordersManagement.createPastriesItem(pItem);

        assertNotNull(orderItem);

        ordersManagement.addItemIntoOrder(orderID, orderItem);
        ordersManagement.finalizeActiveOrder(orderID);

        CustomerOrder order = ordersManagement.getNextOrder();

        ordersManagement.fulfilledOrder(order);

        cafeShop.closeShop();
    }

    
}
