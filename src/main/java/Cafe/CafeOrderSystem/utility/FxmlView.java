package Cafe.CafeOrderSystem.utility;

public enum FxmlView {
    BARISTA("/Cafe/CafeOrderSystem/barista-view.fxml"),
    MANAGER("/Cafe/CafeOrderSystem/manager-view.fxml"),
    CUSTOMER("/Cafe/CafeOrderSystem/customer-view.fxml"),
    //overlay views
    ORDER("/Cafe/CafeOrderSystem/order-overlay.fxml"),
    MENU_ORDER("/Cafe/CafeOrderSystem/menu-overlay.fxml"),
    INGREDIENT("/Cafe/CafeOrderSystem/ingredient-overlay.fxml"),
    //
    HELLO("/Cafe/CafeOrderSystem/hello-view.fxml"),

    // TODO: (Ali) Create Checkout Screen View here
    LOGIN("/Cafe/CafeOrderSystem/login.fxml");
    
    private final String path;
    
    FxmlView(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}


