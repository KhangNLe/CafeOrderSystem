package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;
import Cafe.CafeOrderSystem.Menu.Items.BeverageCost;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import Cafe.CafeOrderSystem.Menu.*;

import java.util.*;
import java.util.logging.Logger;

public class TestManagerCustom {
    private static Cafe cafeShop;
    private static MenuManagement menuManagement;
    private static final Logger LOGGER = Logger.getLogger(TestManagerCustom.class.getName());

    @BeforeAll
    static void setUp() {
        cafeShop = new Cafe();
        cafeShop.startShop();
        menuManagement = cafeShop.getCafeMenuManagement();
    }


    @Test
    @DisplayName("Test for modify a beverage item size")
    void testModifyReplaceBeverageSize() {
        List<BeverageItem> beverages = menuManagement.getBeverageItems();

        BeverageItem latte = beverages.stream()
                .filter(b -> b.name().equalsIgnoreCase("latte"))
                .findFirst().orElseThrow(() -> new InvalidInputException("No latte found"));

        BeverageSize s = new BeverageSize("small");
        BeverageCost small = latte.cost().get(s);

        assertNotNull(small);

        Map<Ingredients, Integer> newIngredients = new HashMap<>();
        newIngredients.put(new Ingredients("coffee"), 4);
        newIngredients.put(new Ingredients("sugar"), 10);
        newIngredients.put(new Ingredients("milk"), 5);

        menuManagement.modifyBeverageSize(beverages.indexOf(latte), s, newIngredients, 100.0);

        List<BeverageItem> newBeverages = menuManagement.getBeverageItems();

        BeverageItem newLatte = newBeverages.stream()
                .filter(b -> b.name().equalsIgnoreCase("latte"))
                .findFirst().orElseThrow(() -> new InvalidInputException("No latte found"));

        assertNotEquals(latte, newLatte);

        BeverageCost newSmall = newLatte.cost().get(s);

        assertNotNull(newSmall);
        assertNotEquals(small, newSmall);
    }

    @Test
    @DisplayName("Test adding a new size into beverage")
    void testAddBeverageSize() {
        List<BeverageItem> beverages = menuManagement.getBeverageItems();

        BeverageItem latte = beverages.stream()
                .filter(b -> b.name().equalsIgnoreCase("latte"))
                .findFirst().orElseThrow(() -> new InvalidInputException("No latte found"));

        BeverageSize xLarge = new BeverageSize("Extra_large");

        Map<Ingredients, Integer> ingredients = new HashMap<>();
        ingredients.put(new Ingredients("coffee"), 4);
        ingredients.put(new Ingredients("sugar"), 10);
        ingredients.put(new Ingredients("milk"), 15);

        menuManagement.modifyBeverageSize(beverages.indexOf(latte), xLarge, ingredients, 15.0);

        beverages = menuManagement.getBeverageItems();

        BeverageItem newLatte = beverages.stream()
                .filter(b -> b.name().equalsIgnoreCase("latte"))
                .findFirst().orElseThrow(() -> new InvalidInputException("No latte found"));

        assertNotNull(newLatte);
        assertNotEquals(latte, newLatte);

        assertNotEquals(latte.cost().size(), newLatte.cost().size());

        LOGGER.info("old Latte: " + latte);
        LOGGER.info("new Latte: " + newLatte);
    }

    @Test
    @DisplayName("Test for modify pastries")
    void testModifyPastriesItem(){
        List<PastriesItem> pastries = menuManagement.getPastriesItems();
        int originalSize = pastries.size();

        PastriesItem cookie = pastries.stream()
                .filter(i -> i.name().equalsIgnoreCase("sugar cookie"))
                .findFirst().orElseThrow(() -> new InvalidInputException("No sugar cookie found"));

        menuManagement.modifyPastriesCost(pastries.indexOf(cookie), 1000);


        PastriesItem newCookie = pastries.stream()
                        .filter(i -> i.name().equalsIgnoreCase("sugar cookie"))
                        .findFirst().orElseThrow(() -> new InvalidInputException("No sugar cookie found"));


        assertNotEquals(cookie, newCookie);
        assertEquals(originalSize,  menuManagement.getPastriesItems().size());

        LOGGER.info("old Cookie " + cookie);
        LOGGER.info("new Cookie " + newCookie);
    }

    @Test
    @DisplayName("Test for modify ingredient cost of Pastries")
    void testModifyPastriesIngredientCost(){
        List<PastriesItem> pastries = menuManagement.getPastriesItems();

        PastriesItem cookie = pastries.stream()
                .filter(i -> i.name().equalsIgnoreCase("sugar cookie"))
                .findFirst().orElseThrow(() -> new InvalidInputException("No sugar cookie found"));

        Map<Ingredients, Integer> ingredients = new HashMap<>();
        ingredients.put(new Ingredients("coffee"), 4);
        ingredients.put(new Ingredients("sugar"), 10);
        ingredients.put(new Ingredients("milk"), 15);

        menuManagement.modifyPastriesIngredientCost(ingredients, pastries.indexOf(cookie));


        PastriesItem newCookie = pastries.stream()
                .filter(i -> i.name().equalsIgnoreCase("sugar cookie"))
                .findFirst().orElseThrow(() -> new InvalidInputException("No sugar cookie found"));

        assertNotNull(newCookie);

        assertNotEquals(cookie, newCookie);

        LOGGER.info("old Cookie ingredients: " + cookie);
        LOGGER.info("new Cookie ingredients: " + newCookie);
    }
}
