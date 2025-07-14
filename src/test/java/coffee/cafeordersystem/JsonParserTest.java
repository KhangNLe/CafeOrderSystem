package coffee.cafeordersystem;

import coffee.cafeordersystem.CatalogItems.Beverage;
import coffee.cafeordersystem.CatalogItems.BeverageSize;
import coffee.cafeordersystem.CatalogItems.Ingredients;
import coffee.cafeordersystem.CatalogItems.Pastries;
import coffee.cafeordersystem.Menu.Items.BeverageCost;
import coffee.cafeordersystem.Menu.Items.BeverageItem;
import coffee.cafeordersystem.Menu.Items.PastriesCost;
import coffee.cafeordersystem.Menu.Items.PastriesItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;

public class JsonParserTest {
    private ObjectMapper MAPPER = new ObjectMapper();

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
    @DisplayName("Test for existin Beverage Item")
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
}
