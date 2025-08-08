package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientList;
import Cafe.CafeOrderSystem.Inventory.Inventory;
import Cafe.CafeOrderSystem.JsonParser.Authentication.AuthenticationParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.*;
import Cafe.CafeOrderSystem.JsonParser.OrderItem.*;

/**
 * Utility class responsible for initializing and registering all parser components
 * used in the Café application.
 *
 * <p>
 * This class centralizes parser construction for menus, inventory, employees, and order history.
 * It wires them into a {@link CafeParser} instance, which manages their lifecycle.
 * </p>
 *
 * <p>
 * The parser directories are hardcoded here but may be extracted into a configuration
 * object for improved flexibility.
 * </p>
 *
 * <p>This class is not meant to be instantiated.</p>
 */
public class ParserManagement {
    private static final String BEVERAGE_DIR = "src/main/resources/InitialCatalog/BeveragesCatalog";
    private static final String PASTRIES_DIR = "src/main/resources/InitialCatalog/PastriesCatalog";
    private static final String ADDON_DIR = "src/main/resources/InitialCatalog/AddonCatalog";
    private static final String INVENTORY_DIR = "src/main/resources/InitialCatalog/Inventory";
    private static final String PENDING_DIR = "src/main/resources/OrderHistory/Pending";
    private static final String HISTORY_DIR = "src/main/resources/OrderHistory/History";
    private static final String EMPLOYEE_DIR = "src/main/resources/EmployeeAcc/";

    /**
     * Private to prevent instantiation
     */
    private ParserManagement(){}

    /**
     * Initializes a new {@link CafeParser} instance with all required parser components registered
     *
     * @return a fully configured CafeParser instance
     */
    public static CafeParser initializeCafeParser() {
        CafeParser cafeParser = new CafeParser();
        addAccounts(cafeParser);
        addOrders(cafeParser);
        addInventoryParser(cafeParser);
        addMenuParser(cafeParser);

        return cafeParser;
    }

    /**
     * Registers menu-related parsers
     *
     * @param cafeParser the parser manager to register with
     */
    private static void addMenuParser(CafeParser cafeParser) {
        Parsers beverage = BeverageParser.create(BEVERAGE_DIR);
        Parsers pastries = PastriesParser.create(PASTRIES_DIR);
        Parsers addOn = AddOnParser.create(ADDON_DIR);

        cafeParser.addParser(beverage);
        cafeParser.addParser(pastries);
        cafeParser.addParser(addOn);
    }

    /**
     * Registers the inventory parser
     *
     * @param cafeParser the parser manager to register with
     */
    private static void addInventoryParser(CafeParser cafeParser) {
        Parsers inventory = new IngredientList(INVENTORY_DIR);

        cafeParser.addParser(inventory);
    }

    /**
     * Register account-related parsers including baristas and managers
     *
     * @param parser the parser manager to register with
     */
    private static void addAccounts(CafeParser parser){
        AuthenticationParser accountsParser = new AuthenticationParser(EMPLOYEE_DIR);

        parser.addParser(accountsParser);
    }

    /**
     * Registers order-related parsers: pending and historical orders
     *
     * @param parser the parser manager to register with
     */
    private static void addOrders(CafeParser parser){
        Parsers pendingOrders = CustomerOrderParser.create(PENDING_DIR);
        Parsers historyOrders = CustomerOrderParser.create(HISTORY_DIR);
        OrdersParser ordersParser = new OrdersParser(pendingOrders, historyOrders);
        parser.addParser(ordersParser);
    }
}
