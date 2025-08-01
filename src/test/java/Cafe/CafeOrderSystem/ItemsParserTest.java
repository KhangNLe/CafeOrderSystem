package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import Cafe.CafeOrderSystem.JsonParser.*;

public class ItemsParserTest {

    @Test
    @DisplayName("Test for parsing Beverage Items")
    void testParseBeverageItems() {
        String dirPath = "src/main/resources/InitialCatalog/BeveragesCatalog/";
        JsonCollection<BeverageItem> beverages = new JsonCollection<>(new ItemsParser(), dirPath, BeverageItem.class);

        beverages.startCollection();

        assertNotNull(beverages.getCollection());
        assertFalse(beverages.getCollection().isEmpty());
    }

    @Test
    @DisplayName("Test for parsing Pastries")
    void testParsePastries() {
        String dirPath = "src/main/resources/InitialCatalog/PastriesCatalog/";

        JsonCollection<PastriesItem> pastries = new JsonCollection<>(new ItemsParser(), dirPath, PastriesItem.class);
        pastries.startCollection();

        assertNotNull(pastries.getCollection());
        assertFalse(pastries.getCollection().isEmpty());
    }
}
