package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.JsonParser.Authentication.AuthenticationParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.AddOnParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.BeverageParser;
import Cafe.CafeOrderSystem.JsonParser.CafeMenu.PastriesParser;
import Cafe.CafeOrderSystem.Menu.CafeMenu;
import Cafe.CafeOrderSystem.Orders.OrdersManagement;
import Cafe.CafeOrderSystem.Roles.EmployeesAuthentication;
import Cafe.CafeOrderSystem.JsonParser.*;

public class Cafe {
    private final CafeParser cafeShop;
    private final CafeMenu menu;
    private EmployeesAuthentication  employees;
    private OrdersManagement ordersManagement;

    public Cafe(){
        cafeShop = ParserManagement.initializeCafeParser();
        menu = getMenu();
        employees = getEmployeeAccounts();
        ordersManagement = new OrdersManagement();
    }

    public void startShop(){
        cafeShop.openCafeShop();
    }

    private CafeMenu getMenu(){
        BeverageParser beverageParser = cafeShop.getParserType(BeverageParser.class);
        PastriesParser pastriesParser = cafeShop.getParserType(PastriesParser.class);
        AddOnParser addOnParser = cafeShop.getParserType(AddOnParser.class);

        return new CafeMenu(beverageParser, pastriesParser, addOnParser);
    }

    private EmployeesAuthentication getEmployeeAccounts(){
        AuthenticationParser authenticationParser =
                cafeShop.getParserType(AuthenticationParser.class);

        return new EmployeesAuthentication(authenticationParser.getBaristaAcc(),
                authenticationParser.getManagerAcc());
    }

}
