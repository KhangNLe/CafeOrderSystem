package Cafe.CafeOrderSystem.JsonParser.OrderItem;

import Cafe.CafeOrderSystem.JsonParser.JsonArrayParser;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;
import Cafe.CafeOrderSystem.Orders.OrdersManagement;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class OrderHistoryParser extends OrdersParser {
    private static final String FILE_PATH = "src/main/resources/OrderHistory/PastOrder.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final File FILE = new File(FILE_PATH);

    public static void addOrderToHistory(CustomerOrder order) {
        try (RandomAccessFile raf = new RandomAccessFile(FILE, "rw")) {
            String jsonData = MAPPER.writeValueAsString(order);

            if (FILE.length() == 0) {
                raf.writeBytes("[\n" + jsonData + "\n]");
            } else {
                raf.seek(FILE.length() - 2);
                raf.writeBytes("\n," + jsonData + "\n]");
            }

        } catch (FileNotFoundException e){
           throw new IllegalArgumentException(
                   String.format("File %s not found", FILE.getAbsolutePath())
           );
        } catch (IOException e){
            throw new IllegalArgumentException(
                    "Error while writing order to json file: " + e.getMessage()
            );
        }
    }

    public static List<CustomerOrder> loadOrderHistory(){
        List<CustomerOrder> orderHistory = new ArrayList<>();
        List<JsonNode> rootNode = JsonArrayParser.parse(FILE_PATH);

        if (rootNode == null) {
            throw new IllegalArgumentException(
                    String.format("There was no orders history file inside %s", FILE_PATH)
            );
        }

        for (JsonNode node : rootNode) {
            CustomerOrder order = getOrder(node);
            validateCustomerOrder(order, FILE_PATH);
            orderHistory.add(order);
        }

        return orderHistory;
    }

    private static CustomerOrder getOrder(JsonNode node){
        if (node == null || node.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("There was no order inside a node from %s", FILE_PATH)
            );
        }

        try{
            return MAPPER.treeToValue(node, CustomerOrder.class);
        } catch (IOException e){
            throw new IllegalArgumentException(
                    String.format("Could not parse node %s into CustomerOrder. Reason %s",
                            node, e.getMessage()
            ));
        }
    }



}
