package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientList;
import Cafe.CafeOrderSystem.Inventory.Inventory;
import Cafe.CafeOrderSystem.JsonParser.Authentication.AuthenticationParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.AddOnParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.BeverageParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.PastriesParser;
import Cafe.CafeOrderSystem.JsonParser.OrderItem.OrdersParser;
import Cafe.CafeOrderSystem.Menu.CafeMenu;
import Cafe.CafeOrderSystem.Menu.MenuManagement;
import Cafe.CafeOrderSystem.Orders.CafeOrders;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;
import Cafe.CafeOrderSystem.Orders.OrdersManagement;
import Cafe.CafeOrderSystem.Roles.EmployeesAuthentication;
import Cafe.CafeOrderSystem.JsonParser.*;

import java.util.List;

/**
 * Represents the central control system for a cafe simulation.
 * <p>
 * The {@code Cafe} class acts as a façade, coordinating various subsystems:
 * <ul>
 *     <li>{@link MenuManagement} - for managing the cafe menu.</li>
 *     <li>{@link EmployeesAuthentication} - for validating employee credentials.</li>
 *     <li>{@link OrdersManagement} - for tracking and fulfilling customer orders.</li>
 *     <li>{@link Inventory} - for managing ingredient stock levels.</li>
 * </ul>
 * <p>
 * All subsystems are initialized automatically in the constructor using
 * the {@link ParserManagement} factory.
 * </p>
 *
 * <p><strong>Lifecycle:</strong></p>
 * <ol>
 *     <li>Construct a new {@code Cafe} instance.</li>
 *     <li>Call {@link #startShop()} to open the cafe.</li>
 *     <li>Perform operations such as processing orders, modifying inventory, etc.</li>
 *     <li>Call {@link #closeShop()} to close the cafe.</li>
 * </ol>
 */
public class Cafe {
    private final CafeParser cafeShop;
    private final MenuManagement  menuManagement;
    private final EmployeesAuthentication  employees;
    private final OrdersManagement ordersManagement;
    private final Inventory cafeInventory;

    /**
     * Constructs and initializes a new {@code Cafe} instance.
     * <p>
     * All subsystems are created based on configuration retrieved from
     * {@link ParserManagement#initializeCafeParser()}.
     * </p>
     *
     * @throws RuntimeException if any required parser cannot be initialized
     */
    public Cafe(){
        cafeShop = ParserManagement.initializeCafeParser();
        menuManagement = createMenuManagement();
        employees = getEmployeeAccounts();
        cafeInventory = createInventoryManagement();
        ordersManagement = createOrdersManagement();

    }

    /**
     * Opens the cafe, enabling operations such as order processing.
     * Delegates to {@link CafeParser#openCafeShop()}.
     */
    public void startShop(){
        cafeShop.openCafeShop();
    }

    /**
     * Closes the cafe and performs any necessary shutdown operations.
     * Delegates to {@link CafeParser#closeShop()}.
     */
    public void closeShop(){
        cafeShop.closeShop();
    }

    /**
     * Retrieves a list of all currently pending customer orders.
     *
     * @return a list of pending {@link CustomerOrder} objects
     */
    public List<CustomerOrder> getPendingOrders(){
        return ordersManagement.getPendingOrder();
    }

    /**
     * Retrieves the complete order history.
     *
     * @return a list of historical {@link CustomerOrder} objects
     */
    public List<CustomerOrder> getOrderHistory(){
        return ordersManagement.getOrderHistory();
    }

    /**
     * Provides access to the cafe's menu management subsystem.
     *
     * @return the {@link MenuManagement} instance
     */
    public MenuManagement getCafeMenuManagement(){
        return menuManagement;
    }

    /**
     * Provides access to the cafe's order management subsystem.
     *
     * @return the {@link OrdersManagement} instance
     */
    public OrdersManagement getOrdersManagement(){
        return ordersManagement;
    }

    /**
     * Provides access to the cafe's employee authentication subsystem.
     *
     * @return the {@link EmployeesAuthentication} instance
     */
    public EmployeesAuthentication getEmployeesAuthentication(){
        return employees;
    }

    /**
     * Provides access to the cafe's inventory management subsystem.
     *
     * @return the {@link Inventory} instance
     */
    public Inventory getInventoryManagement(){
        return cafeInventory;
    }

    /**
     * Returns the list of ingredients currently tracked in the inventory.
     *
     * @return an {@link IngredientList} object
     */
    public IngredientList getIngredientList(){return cafeInventory.getList();}

    /**
     * Modifies the stock level of a specific ingredient.
     *
     * @param ingredient    the ingredient to modify
     * @param amountChanged the amount to add (positive) or remove (negative) from inventory
     * @return {@code true} if the modification succeeded, {@code false} otherwise
     */
    public boolean modifyIngredient(IngredientItem ingredient, int amountChanged){
        return cafeInventory.modifyInventory(amountChanged, ingredient);
    }

    private MenuManagement createMenuManagement(){
        BeverageParser beverageParser = cafeShop.getParserType(BeverageParser.class);
        PastriesParser pastriesParser = cafeShop.getParserType(PastriesParser.class);
        AddOnParser addOnParser = cafeShop.getParserType(AddOnParser.class);

        return new MenuManagement(new CafeMenu(beverageParser, pastriesParser, addOnParser));
    }

    private OrdersManagement createOrdersManagement(){
        OrdersParser ordersParser = cafeShop.getParserType(OrdersParser.class);
        CafeOrders orders = new CafeOrders(
                ordersParser.getPendingOrdersParser(),
                ordersParser.getOrdersHistoryParser()
        );

        return new  OrdersManagement(orders, cafeInventory);
    }

    private EmployeesAuthentication getEmployeeAccounts(){
        AuthenticationParser authenticationParser =
                cafeShop.getParserType(AuthenticationParser.class);

        return new EmployeesAuthentication(authenticationParser);
    }

    private Inventory createInventoryManagement(){
        IngredientList ingredientList = cafeShop.getParserType(IngredientList.class);

        return new Inventory(ingredientList);
    }
}
