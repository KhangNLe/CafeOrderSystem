package coffee.cafeordersystem.JsonParser;



import coffee.cafeordersystem.CatalogItems.Pastries;

import java.util.*;
public class MenuParser {
    private static final String RESOURCES = "src/main/resources/InitialCatalog/";
    private static final String BEVERAGES = RESOURCES + "BeveragesCatalog/";
    private static final String PASTRIES = RESOURCES + "PastriesCatalog/";
    private MenuParser() {}

    public static void initializeMenuCatalog(){
        initializeBeveragesCatalog();
        initializePastriesCatalog();
        initializeBeveragesAddOn();
    }

    private static void initializeBeveragesCatalog(){
       List<String> files = List.of(
               BEVERAGES + "CoffeeCatalog.json",
               BEVERAGES + "TeaCatalog.json"
       );
       parsingMenuBeverages(files);
    }

    private static void initializePastriesCatalog(){
        List<String> files = List.of(
                PASTRIES + "CookiesCatalog.json",
                PASTRIES + "CroissantCatalog.json",
                PASTRIES + "MuffinCatalog.json"
        );
        parsingMenuPastries(files);
    }

    private static void initializeBeveragesAddOn(){
        String file = BEVERAGES + "CustomizationCatalog.json";
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
