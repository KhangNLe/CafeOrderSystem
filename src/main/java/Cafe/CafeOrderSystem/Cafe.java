package Cafe.CafeOrderSystem;

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

// Facade
public class Cafe {
    private final CafeParser cafeShop;
    private final MenuManagement  menuManagement;
    private final EmployeesAuthentication  employees;
    private final OrdersManagement ordersManagement;

    public Cafe(){
        cafeShop = ParserManagement.initializeCafeParser();
        menuManagement = createMenuManagement();
        employees = getEmployeeAccounts();
        ordersManagement = createOrdersManagement();
    }

    public void startShop(){
        cafeShop.openCafeShop();
    }

    public void closeShop(){
        cafeShop.closeShop();
    }

    public List<CustomerOrder> getPendingOrders(){
        return ordersManagement.getPendingOrder();
    }

    public List<CustomerOrder> getOrderHistory(){
        return ordersManagement.getOrderHistory();
    }

    public MenuManagement getCafeMenuManagement(){
        return menuManagement;
    }

    public OrdersManagement getOrdersManagement(){
        return ordersManagement;
    }

    public EmployeesAuthentication getEmployeesAuthentication(){
        return employees;
    }

    public OrdersManagement getOrderManagement(){
        return ordersManagement;
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

        return new  OrdersManagement(orders);
    }

    private EmployeesAuthentication getEmployeeAccounts(){
        AuthenticationParser authenticationParser =
                cafeShop.getParserType(AuthenticationParser.class);

        return new EmployeesAuthentication(authenticationParser);
    }

}
