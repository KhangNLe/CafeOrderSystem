package Cafe.CafeOrderSystem.JsonParser.CafeMenu;

import java.util.*;
public class MenuParser {
    private static final String RESOURCES = "src/main/resources/InitialCatalog/";
    private static final String BEVERAGES_PATH = RESOURCES + "BeveragesCatalog/";
    private static final String PASTRIES_PATH = RESOURCES + "PastriesCatalog/";
    private MenuParser() {}

    public static void initializeMenuCatalog(){
        initializeBeveragesCatalog();
        initializePastriesCatalog();
        initializeBeveragesAddOn();
    }

    private static void initializeBeveragesCatalog(){
       List<String> files = List.of(
               BEVERAGES_PATH + "CoffeeCatalog.json",
               BEVERAGES_PATH + "TeaCatalog.json"
       );
       parsingMenuBeverages(files);
    }

    private static void initializePastriesCatalog(){
        List<String> files = List.of(
                PASTRIES_PATH + "CookiesCatalog.json",
                PASTRIES_PATH + "CroissantCatalog.json",
                PASTRIES_PATH + "MuffinCatalog.json"
        );
        parsingMenuPastries(files);
    }

    private static void initializeBeveragesAddOn(){
        String file = BEVERAGES_PATH + "CustomizationCatalog.json";
        BeverageCustomizeParser.initializeBeverageAddOn(file);
    }

    private static void parsingMenuPastries(List<String> pastriesFilePaths) {
        for (String filePath : pastriesFilePaths) {
            PastriesParser.parsePastries(filePath);
        }
    }

    private static void parsingMenuBeverages(List<String> beverageFilePaths) {
        for (String filePath : beverageFilePaths) {
            BeverageParser.initializeMenuBeverages(filePath);
        }
    }
}
