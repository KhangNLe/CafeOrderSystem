package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.JsonParser.Authentication.AuthenticationParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.*;
import Cafe.CafeOrderSystem.JsonParser.OrderItem.*;
import Cafe.CafeOrderSystem.Roles.BaristaRole;
import Cafe.CafeOrderSystem.Roles.ManagerRole;
import Cafe.CafeOrderSystem.Roles.RolesList;

public class ParserManagement {
    private static final String BEVERAGE_DIR = "src/main/resources/InitialCatalog/BeveragesCatalog";
    private static final String PASTRIES_DIR = "src/main/resources/InitialCatalog/PastriesCatalog";
    private static final String ADDON_DIR = "src/main/resources/InitialCatalog/AddonCatalog";
    private static final String INVENTORY_DIR = "src/main/resources/InitialCatalog/Inventory";
    private static final String PENDING_DIR = "src/main/resources/OrderHistory/Pending";
    private static final String HISTORY_DIR = "src/main/resources/OrderHistory/History";
    private static final String BARISTA_DIR = "src/main/resources/EmployeeAcc/Baristas";
    private static final String MANAGER_DIR =  "src/main/resources/EmployeeAcc/Managers";

    public static CafeParser initializeCafeParser() {
        CafeParser cafeParser = new CafeParser();
        addAccounts(cafeParser);
        addOrders(cafeParser);
        addInventoryParser(cafeParser);
        addMenuParser(cafeParser);

        return cafeParser;
    }

    private static void addMenuParser(CafeParser cafeParser) {
        Parsers beverage = BeverageParser.create(BEVERAGE_DIR);
        Parsers pastries = PastriesParser.create(PASTRIES_DIR);
        Parsers addOn = AddOnParser.create(ADDON_DIR);

        cafeParser.addParser(beverage);
        cafeParser.addParser(pastries);
        cafeParser.addParser(addOn);
    }

    private static void addInventoryParser(CafeParser cafeParser) {
        Parsers inventory = InventoryParser.create(INVENTORY_DIR);

        cafeParser.addParser(inventory);
    }

    private static void addAccounts(CafeParser parser){
        RolesList<BaristaRole> barista = RolesList.newBaristaRoleList(BARISTA_DIR);
        RolesList<ManagerRole> manager = RolesList.newManagerRoleList(MANAGER_DIR);
        AuthenticationParser accountsParser = new AuthenticationParser(manager, barista);

        parser.addParser(accountsParser);
    }

    private static void addOrders(CafeParser parser){
        Parsers pendingOrders = CustomerOrderParser.create(PENDING_DIR);
        Parsers historyOrders = CustomerOrderParser.create(HISTORY_DIR);
        OrdersParser ordersParser = new OrdersParser(pendingOrders, historyOrders);
        parser.addParser(ordersParser);
    }
}
