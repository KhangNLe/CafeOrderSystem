package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;
import Cafe.CafeOrderSystem.Menu.Items.BeverageCost;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.CustomItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import Cafe.CafeOrderSystem.Menu.*;
import Cafe.CafeOrderSystem.Orders.*;

import java.util.*;
import java.util.logging.Logger;

public class OrderingTest {
    private static Cafe cafeShop;
    private static OrdersManagement ordersManagement;
    private static Logger LOGGER = Logger.getLogger(OrderingTest.class.getName());

    @BeforeAll
    static void setUp(){
        cafeShop = new Cafe();
        cafeShop.startShop();
        ordersManagement = cafeShop.getOrdersManagement();
    }

    @Test
    @DisplayName("Test for creating a customer order")
    void testCustomerOrderID(){
        String orderID = ordersManagement.createNewOrder();
        String order2 = ordersManagement.createNewOrder();
        String order3 = ordersManagement.createNewOrder();

        CustomerOrder order = ordersManagement.getNextOrder();
        assertEquals(orderID, order.getOrderID());

        order = ordersManagement.getNextOrder();
        assertEquals(order2, order.getOrderID());

        order = ordersManagement.getNextOrder();
        assertEquals(order3, order.getOrderID());
    }

    @Test
    @DisplayName("Test for adding item into customer order")
    void testAddingItem(){
        String orderID = ordersManagement.createNewOrder();

        MenuManagement menu = cafeShop.getCafeMenuManagement();
        BeverageItem item = menu.getBeverageItem(3);

        assertNotNull(item);
        LOGGER.info("Beverage Item: " + item);

        BeverageSize med = new BeverageSize("medium");
        BeverageCost cost = item.cost().get(med);
        assertNotNull(cost);

        OrderItem orderItem = ordersManagement.createBeverageItem(item, med, null);
        assertNotNull(orderItem);

        ordersManagement.addItemIntoOrder(orderID, orderItem);

        ordersManagement.finalizeActiveOrder(orderID);

        CustomerOrder order = ordersManagement.getNextOrder();
        assertEquals(orderID, order.getOrderID());
        assertTrue(order.getTotalPrice() > 0);

        LOGGER.info("Customer Order Item: " + order);

    }

    @Test
    @DisplayName("Test with modify beverage order")
    void testModifyBeverageOrder(){
        String orderID = ordersManagement.createNewOrder();
        MenuManagement menu = cafeShop.getCafeMenuManagement();

        List<BeverageItem> beverages = menu.getBeverageItems();
        List<CustomItem> customItems = menu.getAddOnItems();

        BeverageItem greenTea = beverages.stream()
                .filter(i -> i.name().trim().equalsIgnoreCase("Green Tea"))
                .findFirst().orElseThrow( () -> new InvalidInputException(
                        "Could " +
                        "not find green tea"));

        CustomItem decaf = customItems.stream()
                .filter(i -> i.name().equalsIgnoreCase("decaf"))
                .findFirst().orElseThrow(()-> new InvalidInputException("Could not find decaf"));

        BeverageSize med = new BeverageSize("medium");
        OrderItem orderItem = ordersManagement.createBeverageItem(greenTea.copyOf(), med
                , decaf);
        OrderItem orderItem2 = ordersManagement.createBeverageItem(greenTea.copyOf(), med
                , null);

        assertNotNull(orderItem);
        assertNotNull(orderItem2);

        assertTrue(orderItem2.getPrice() < orderItem.getPrice());

        ordersManagement.addItemIntoOrder(orderID, orderItem);
        ordersManagement.addItemIntoOrder(orderID, orderItem2);

        ordersManagement.finalizeActiveOrder(orderID);

        CustomerOrder order = ordersManagement.getNextOrder();

        assertEquals(orderID, order.getOrderID());
        assertTrue(order.getTotalPrice() > 0);

        LOGGER.info("Customer Order Item: " + order);
    }

    @Test
    @DisplayName("Test for Pastries Item")
    void testPastriesItem(){
        String orderID = ordersManagement.createNewOrder();
        MenuManagement menu = cafeShop.getCafeMenuManagement();

        List<PastriesItem> pastries = menu.getPastriesItems();

        PastriesItem cookie = pastries.stream().filter(i ->
                i.name().equalsIgnoreCase("sugar cookie"))
                .findFirst().orElseThrow(() ->
                        new InvalidInputException("Could not find sugar cookie"));


        OrderItem pastry = ordersManagement.createPastriesItem(cookie.copyOf());

        assertNotNull(pastry);
        ordersManagement.addItemIntoOrder(orderID, pastry);
        ordersManagement.finalizeActiveOrder(orderID);

        CustomerOrder order = ordersManagement.getNextOrder();
        assertNotNull(order);
        assertTrue(order.getTotalPrice() > 0);
        assertEquals(1, order.getOrderItems().size());

        LOGGER.info("Pastries Order Item: " + order);
    }
}
