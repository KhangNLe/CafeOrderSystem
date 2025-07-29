package Cafe.CafeOrderSystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

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
    }
}
