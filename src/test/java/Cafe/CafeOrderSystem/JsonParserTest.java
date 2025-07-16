package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.Authentication.EmployeesAuthentication;
import Cafe.CafeOrderSystem.JsonParser.*;
import Cafe.CafeOrderSystem.Menu.Items.*;
import Cafe.CafeOrderSystem.Menu.CafeMenu;
import Cafe.CafeOrderSystem.Inventory.*;
import Cafe.CafeOrderSystem.CatalogItems.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class JsonParserTest {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private static Logger LOGGER = Logger.getLogger(JsonParserTest.class.getName());
    private final String resources = "src/main/resources/InitialCatalog/";

    @Test
    @DisplayName("Test for existing Pastry Item")
    void testExistingPastryItem() {
        String json = """
          {
            "id": "P-C01",
            "name": "Sugar Cookie",
            "type": "COOKIE",
            "cost": {
              "price": 2,
              "ingredients": {
                "BUTTER": 1,
                "FLOUR": 1,
                "SUGAR": 1
              }
            }
          }
        """;

        PastriesItem item = null;
        try {
            item = MAPPER.readValue(json, PastriesItem.class);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        if (item == null){
            fail("Item did not created");
        }
        String expectedId = "P-C01";
        String expectedName = "Sugar Cookie";
        double expectedPrice = 2.0;

        assertEquals(expectedId, item.id());
        assertEquals(expectedName, item.name());
        assertEquals(Pastries.COOKIE, item.type());

        PastriesCost cost = item.cost();
        if (cost == null){
            fail("Cost did not created");
        }

        Map<Ingredients, Integer> expectedIngredients = new HashMap<>();
        expectedIngredients.put(Ingredients.BUTTER, 1);
        expectedIngredients.put(Ingredients.FLOUR, 1);
        expectedIngredients.put(Ingredients.SUGAR, 1);

        assertEquals(expectedIngredients, cost.ingredients());
        assertEquals(expectedPrice, cost.price(), 0.001);

    }

    @Test
    @DisplayName("Test for existing Beverage Item")
    void testExistingBeverageItem() {
        String json = """
          {
            "id": "B-C01",
            "name": "Latte",
            "type": "COFFEE",
            "cost": {
              "SMALL": {
                "price": 5,
                "ingredients": {
                  "COFFEE_BEAN": 3,
                  "MILK": 3,
                  "SUGAR": 0
                }
              },
              "MEDIUM": {
                "price": 7,
                "ingredients": {
                  "COFFEE_BEAN": 4,
                  "MILK": 4,
                  "SUGAR": 1
                }
              },
              "LARGE": {
                "price": 8,
                "ingredients": {
                  "COFFEE_BEAN": 5,
                  "MILK": 4,
                  "SUGAR": 1
                }
              }
            }
          }
        """;

        BeverageItem item = null;
        try{
            item = MAPPER.readValue(json, BeverageItem.class);
        } catch (IOException e){
            fail(e.getMessage());
        }

        if (item == null){
            fail("Item did not created");
        }

        String expectedId = "B-C01";
        String expectedName = "Latte";
        double[] expectedPrices = {5.0, 7.0, 8.0};
        int idx = 0;

        assertEquals(expectedId, item.id());
        assertEquals(expectedName, item.name());
        assertEquals(Beverage.COFFEE, item.type());

        Set<BeverageSize> expectedSizes = Set.of(BeverageSize.SMALL, BeverageSize.MEDIUM, BeverageSize.LARGE);
        Set<Ingredients> ingredients = Set.of(Ingredients.COFFEE_BEAN, Ingredients.MILK, Ingredients.SUGAR);
        assertEquals(expectedSizes, item.cost().keySet());
        List<BeverageSize> sizes = List.of(BeverageSize.SMALL, BeverageSize.MEDIUM, BeverageSize.LARGE);

        for (BeverageSize size : sizes){
            BeverageCost cost = item.cost().get(size);
            assertEquals(ingredients, cost.ingredients().keySet());
            assertEquals(expectedPrices[idx++], cost.price(), 0.001);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"CookiesCatalog.json", "CroissantCatalog.json", "MuffinCatalog.json"})
    @DisplayName("Test for full parsing of Pastries Items")
    void testFullParsingOfPastriesItems(String file) {
        String filePath = resources + "PastriesCatalog/" + file;

        CafeMenu.destroyInstance();
        PastriesParser.parsePastries(filePath);
        CafeMenu cafe = CafeMenu.getInstance();

        assertNotNull(cafe.getPastriesItems());

    }

    @ParameterizedTest
    @ValueSource(strings = {"CoffeeCatalog.json", "TeaCatalog.json"})
    @DisplayName("Test for full parsing of Beverages items")
    void testFullParsingOfBeveragesItems(String file) {
        String filePath = resources + "BeveragesCatalog/" + file;

        CafeMenu.destroyInstance();
        BeverageParser.initializeMenuBeverages(filePath);
        CafeMenu cafe = CafeMenu.getInstance();

        assertNotNull(cafe.getBeveragesItems());
    }

    @Test
    @DisplayName("Test for parsing beverage add-on")
    void testParseBeverageAddOn() {
        String filePath = resources + "BeveragesCatalog/CustomizationCatalog.json";
        CafeMenu.destroyInstance();

        BeverageCustomizeParser.initializeBeverageAddOn(filePath);
        CafeMenu cafe = CafeMenu.getInstance();

        assertNotNull(cafe.getBeveragesItems());
    }

    @Test
    @DisplayName("Test for full menu initialization")
    void testFullMenuInitialization() {
        CafeMenu.destroyInstance();
        MenuParser.initializeMenuCatalog();
        CafeMenu cafe = CafeMenu.getInstance();

        assertNotNull(cafe.getBeveragesItems());
        assertNotNull(cafe.getPastriesItems());
        assertNotNull(cafe.getBeverageAddOn());

        assertFalse(cafe.getBeverageAddOn().isEmpty());
        assertFalse(cafe.getPastriesItems().isEmpty());
        assertFalse(cafe.getBeverageAddOn().isEmpty());
    }

    @Test
    @DisplayName("Test for initiate inventory")
    void testInitiateInventory() {
        CafeInventory cafe = CafeInventory.getInstance();
        assertNotNull(cafe);

        InventoryParser.initializeCafeInventory();

        assertNotNull(cafe.getInventory());
        assertFalse(cafe.getInventory().isEmpty());
        assertEquals(19, cafe.getInventory().size());
    }

    @Test
    @DisplayName("Test for initiate employee authentication")
    void testInitiateEmployeeAuthentication() {
        EmployeesAuthentication employees = EmployeesAuthentication.getInstance();

        assertNotNull(employees);

        AuthenticationParser.initializeAccounts();

        try {
            employees.addBaristaAccount("Barista", "Barista");
        } catch (IllegalStateException e) {
            fail(e.getMessage());
        }

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            employees.addBaristaAccount("barista1", "barista1");
        });

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> {
           employees.addManagerAccount("manager1", "manager1");
        });

        LOGGER.info(e.getLocalizedMessage() + "\n" + e2.getMessage());
    }

    @Test
    @DisplayName("Test for initialize the Cafe Shop")
    void testInitializeCafeShop() {
        try {
            CafeParser.openCafeShop();
        } catch (IllegalStateException e) {
            fail(e.getMessage());
        }
    }
}
