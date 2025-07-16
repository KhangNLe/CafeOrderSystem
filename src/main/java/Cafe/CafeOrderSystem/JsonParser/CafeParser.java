package Cafe.CafeOrderSystem.JsonParser;

public class CafeParser {
    private CafeParser() {}

    public static void openCafeShop(){
        MenuParser.initializeMenuCatalog();
        InventoryParser.initializeCafeInventory();
        AuthenticationParser.initializeAccounts();
    }
}
