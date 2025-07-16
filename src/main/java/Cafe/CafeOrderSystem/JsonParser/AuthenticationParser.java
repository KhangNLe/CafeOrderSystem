package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.Authentication.EmployeesAuthentication;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class AuthenticationParser {
    private static final String BARISTA_PATH = "src/main/resources/EmployeeAcc/Baristas/Accounts.json";
    private static final String MANAGER_PATH = "src/main/resources/EmployeeAcc/Managers/Accounts.json";
    private static final String USER_NAME = "userName";
    private static final String PASSWORD = "password";

    private AuthenticationParser(){}

    public static void initializeAccounts(){
        EmployeesAuthentication employees = EmployeesAuthentication.getInstance();
        parseBaristas(employees);
        parseManagers(employees);

    }

    private static void parseBaristas(EmployeesAuthentication employees){
        List<JsonNode> rootNode = JsonArrayParser.parse(BARISTA_PATH);
        verifyRootNode(rootNode, BARISTA_PATH);

        for (JsonNode node : rootNode) {
            Account acc = verifyAccount(node, BARISTA_PATH);
            employees.addBaristaAccount(acc.name(), acc.password());
        }
    }

    private static void parseManagers(EmployeesAuthentication employees){
        List<JsonNode> rootNode = JsonArrayParser.parse(MANAGER_PATH);
        verifyRootNode(rootNode, MANAGER_PATH);

        for (JsonNode node : rootNode) {
            Account acc = verifyAccount(node, MANAGER_PATH);
            employees.addManagerAccount(acc.name(), acc.password());
        }
    }

    private static void verifyRootNode(List<JsonNode> rootNode, String filePath){
        if (rootNode == null || rootNode.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("File %s does not contain any JSON information", filePath)
            );
        }
    }

    private record Account(String name, String password){}

    private static Account verifyAccount(JsonNode node, String filePath){
        if (node == null){
            throw new IllegalArgumentException(
                    String.format("Invalid json file: %s at", filePath)
            );
        }

        if (!node.hasNonNull(USER_NAME) || !node.hasNonNull(PASSWORD)){
            throw new IllegalArgumentException(
                    String.format("Data inside %s is missing a user name or password file", filePath)
            );
        }

        String name = node.get(USER_NAME).asText();
        String password = node.get(PASSWORD).asText();

        if (name.isEmpty() || password.isEmpty()){
            throw new IllegalArgumentException(
                    String.format("Data inside %s is missing a user name or password information", filePath)
            );
        }

        return new Account(name, password);
    }
}
