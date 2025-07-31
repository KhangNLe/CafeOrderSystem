package Cafe.CafeOrderSystem.JsonParser.OrderItem;

import Cafe.CafeOrderSystem.JsonParser.JsonArrayParser;
import Cafe.CafeOrderSystem.Orders.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SequenceWriter;

import java.util.*;
import java.io.*;

public class PendingOrderParser extends OrdersParser {
    private static final String FILE_PATH = "src/main/resources/OrderHistory/PendingOrders.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final File FILE = new File(FILE_PATH);
    private static final CafeOrders cafeOrders = CafeOrders.getInstance();

    private PendingOrderParser(){}

    public static void savePendingOrders(){
        List<CustomerOrder> orders = cafeOrders.getPendingOrders();
        addOrderToFile(orders);
    }

    private static void addOrderToFile(List<CustomerOrder> order){
        try {
            SequenceWriter writer = MAPPER.writerWithDefaultPrettyPrinter().writeValues(FILE);
            writer.write(order);
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    String.format("Failed to write pending order to file '%s', reason: %s",
                            FILE.getAbsolutePath(), e.getMessage())
            );
        }
    }

    public static void getPendingOrders(){
        cafeOrders.clearPending(); // Clear old entries first
        List<JsonNode> orders = JsonArrayParser.parse(FILE_PATH);
        parseOrders(orders);
    }
    
    private static void parseOrders(List<JsonNode> orders){
        for (JsonNode order : orders) {
            CustomerOrder item = getCustomerOrder(order);
            validateCustomerOrder(item, FILE_PATH);
            cafeOrders.putInPendingOrder(item);
        }
    }

    private static CustomerOrder getCustomerOrder(JsonNode order){
        if (order == null || order.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Could not parse Customer Item node from %s", FILE.getAbsolutePath()
            ));
        }

        try{
            return MAPPER.treeToValue(order, CustomerOrder.class);
        } catch (IOException e){
            throw new IllegalArgumentException(
                    String.format("Could not parse %s into Customer Order from %s", order, FILE.getAbsolutePath())
            );
        }
    }
}
