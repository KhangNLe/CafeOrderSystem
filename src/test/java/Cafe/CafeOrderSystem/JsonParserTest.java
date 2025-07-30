package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.Menu.CafeMenu;
import Cafe.CafeOrderSystem.Menu.MenuManagement;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonParserTest {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private static Logger LOGGER = Logger.getLogger(JsonParserTest.class.getName());
    private final String resources = "src/main/resources/InitialCatalog/";

    @Test
    @DisplayName("Test for starting cafe shop")
    void testForStartingCafeShop(){
        Cafe cafeShop = new Cafe();
        cafeShop.startShop();

        MenuManagement menuManagement = cafeShop.getCafeMenuManagement();
        assertFalse(menuManagement.getPastriesItems().isEmpty());
        assertFalse(menuManagement.getBeverageItems().isEmpty());
        assertFalse(menuManagement.getAddOnItems().isEmpty());

        LOGGER.info(menuManagement.getBeverageItems().toString());
    }
}
