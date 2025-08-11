package Cafe.CafeOrderSystem.utility;

import java.io.IOException;
import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;
import Cafe.CafeOrderSystem.UI.BaristaUiController;
import Cafe.CafeOrderSystem.UI.CustomerUiController;
import Cafe.CafeOrderSystem.UI.HelloApplication;
import Cafe.CafeOrderSystem.UI.HelloController;
import Cafe.CafeOrderSystem.UI.IngredientOverlayController;
import Cafe.CafeOrderSystem.UI.LoginController;
import Cafe.CafeOrderSystem.UI.ManagerController;
import Cafe.CafeOrderSystem.UI.MenuOverlayController;
import Cafe.CafeOrderSystem.UI.OrderOverlayController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadFXML {
    
    private final FXMLLoader loader;
    private final Cafe cafe;
    private final Stage stage;
    private final FxmlView view;
    private final double width;
    private final double height;
    private CustomerOrder order;
    private Object menuItem;
    private IngredientItem ingredient;
    private Runnable refreshCallback;
    
    public LoadFXML(Cafe cafe, Stage stage, FxmlView view, double width, double height) {
        this.loader = new FXMLLoader();
        this.cafe = cafe;
        this.stage = stage;
        this.view = view;
        this.width = width;
        this.height = height;
    }

    // Specialized constructor for order overlays
    public LoadFXML(Cafe cafe, Stage stage, FxmlView view, 
                   double width, double height, 
                   CustomerOrder order, Runnable refreshCallback) {
        this(cafe, stage, view, width, height);
        this.order = order;
        this.refreshCallback = refreshCallback;
    }

    // Specialized constructor for ingredients overlays
    public LoadFXML(Cafe cafe, Stage stage, FxmlView view, 
                   double width, double height, 
                   IngredientItem ingredients, Runnable refreshCallback) {
        this(cafe, stage, view, width, height);
        this.ingredient = ingredients;
        this.refreshCallback = refreshCallback;
    }

        // Specialized constructor for order overlays
    public LoadFXML(Cafe cafe, Stage stage, FxmlView view, 
                   double width, double height, 
                   Object menuItem, Runnable refreshCallback) {
        this(cafe, stage, view, width, height);
        this.menuItem = menuItem;
        this.refreshCallback = refreshCallback;
    }

    
    public void load() throws IOException {
        validateDependencies();
        loadFxml();
        configureController();
        showStage();
    }
    
    private void validateDependencies() {
        if (cafe == null) throw new IllegalStateException("Cafe facade cannot be null");
        if (stage == null) throw new IllegalStateException("Stage cannot be null");
        if (view == null) throw new IllegalStateException("FxmlView cannot be null");
    }
    
    private void loadFxml() throws IOException {
        loader.setLocation(getClass().getResource(view.getPath()));
        Parent root = loader.load();
        stage.setScene(new Scene(root, width, height));
    }
    
    private void configureController() {
        Object controller = loader.getController();
        
        if (controller instanceof BaristaUiController) {
            BaristaUiController baristaController = (BaristaUiController) controller;
            baristaController.setFacade(cafe);
            baristaController.setPrimaryStage(stage);
            baristaController.initAfterInjection();
        }
        if (controller instanceof ManagerController) {
            ManagerController managerController = (ManagerController) controller;
            managerController.setFacade(cafe);
            managerController.setPrimaryStage(stage);
        }
        if (controller instanceof CustomerUiController) {
            CustomerUiController customerController = (CustomerUiController) controller;
            customerController.setFacade(cafe);
            customerController.setPrimaryStage(stage);
        }
        if (controller instanceof LoginController) {
            LoginController loginController = (LoginController) controller;
            loginController.setFacade(cafe);
            loginController.setPrimaryStage(stage);
        }
        if (controller instanceof HelloController) {
            HelloController helloController = (HelloController) controller;
            helloController.setFacade(cafe);
            helloController.setPrimaryStage(stage);
        }
    
        if (controller instanceof OrderOverlayController) {
            OrderOverlayController overlayController = (OrderOverlayController) controller;
            overlayController.setFacade(cafe);
            
            if (order != null && stage != null && refreshCallback != null) {
                // Configure as modal overlay
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                overlayController.setOrderData(order, stage, refreshCallback);
            }
        }
        if (controller instanceof IngredientOverlayController) {
            IngredientOverlayController overlayController = (IngredientOverlayController) controller;
            overlayController.setFacade(cafe);
            
            if (ingredient != null && stage != null && refreshCallback != null) {
                // Configure as modal overlay
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                overlayController.setIngredientData(ingredient, stage, refreshCallback);
            }
        }
        if (controller instanceof MenuOverlayController) {
        MenuOverlayController overlayController = (MenuOverlayController) controller;
        overlayController.setFacade(cafe);
        
        if (menuItem != null && stage != null && refreshCallback != null) {  // Changed from ingredient to menuItem
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            
            overlayController.setMenuItemData(menuItem, stage, refreshCallback);  // Pass menuItem instead of controller
        }
    }
         
        // Add other controller types as needed
    }
    
    private void showStage() {
        stage.setTitle(view.name() + " Dashboard");
        stage.show();

    }
        public static void loadOrderOverlay(Cafe cafe, CustomerOrder order, 
                                      Runnable refreshCallback) throws IOException {
        Stage overlayStage = new Stage();
        LoadFXML loader = new LoadFXML(
            cafe, 
            overlayStage, 
            FxmlView.ORDER,
            400,  // Appropriate width for overlay
            600,  // Appropriate height for overlay
            order, 
            refreshCallback 
        );
        loader.load();
    }

    public static void loadMenuOverlay(Cafe cafe, Object menuItem, 
                                      Runnable refreshCallback) throws IOException {
        Stage overlayStage = new Stage();
        LoadFXML loader = new LoadFXML(
            cafe, 
            overlayStage, 
            FxmlView.MENU_ORDER,
            800,  // Appropriate width for overlay
            800,  // Appropriate height for overlay
            menuItem, 
            refreshCallback 
        );
        loader.load();
    }


public static void loadIngredientOverlay(Cafe cafe, IngredientItem ingredient, 
                                     Runnable refreshCallback) throws IOException {
    Stage overlayStage = new Stage();
    LoadFXML loader = new LoadFXML(
        cafe, 
        overlayStage, 
        FxmlView.INGREDIENT,
        400,  // Width
        300,  // Height (adjusted for ingredient overlay)
        ingredient, 
        refreshCallback 
    );
    loader.load();
}
}