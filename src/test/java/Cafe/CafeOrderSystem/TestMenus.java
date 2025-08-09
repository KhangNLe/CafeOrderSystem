package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.CustomItem;
import Cafe.CafeOrderSystem.Menu.MenuManagement;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class TestMenus {
    private static Cafe cafeShop;
    private static Logger log = Logger.getLogger(TestMenus.class.getName());

    @BeforeAll
    static void setUp(){
        cafeShop = new Cafe();
    }

    @Test
    @DisplayName("Test getting Beverage Items")
    void testBeverageItems(){
        cafeShop.startShop();
        MenuManagement menu = cafeShop.getCafeMenuManagement();

        List<BeverageItem> beverageItems = menu.getBeverageItems();
        List<CustomItem> addOns = menu.getAddOnItems();

        assertFalse(beverageItems.isEmpty());
        assertFalse(addOns.isEmpty());

        Map<BeverageItem, List<CustomItem>> beverages = menu.getBeverageWithCustomizeOption();

        assertFalse(beverages.isEmpty());

        StringBuilder sb = new StringBuilder();
        beverages.forEach((k, v) -> {
            sb.append(
                    String.format("Item: %s\nCustomize: %s\n", k, v)
            );
        });

        log.info(sb.toString());
    }


    @Test
    void testOpenCloseShop(){
        cafeShop.startShop();
        cafeShop.closeShop();
    }
}
