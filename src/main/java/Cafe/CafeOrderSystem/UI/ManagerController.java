package Cafe.CafeOrderSystem.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManagerController {

    @FXML private Label welcomeLabel;
    @FXML private Button menuItemsButton;
    @FXML private Button inventoryButton;
    @FXML private Button fulfilledOrdersButton;
    @FXML private Button addNewItemButton;
    @FXML private Button outButton;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox menuItemsContainer;


    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void handleMenuItems() {
        // Add logic here if needed
    }

    @FXML
    private void handleInventory() {
        // Add logic here if needed
    }

    @FXML
    private void getFulfilledOrders() {

    }

    @FXML
    private void handleAddNewItem() {
        // Just a hook. Actual adding logic would be manual in FXML or custom method.
    }

    @FXML
    private void handleQuit() {
        // Close or log out
    }

    @FXML
    private void initialize() {
        welcomeLabel.setText("Welcome Manager Jane Doe");
        // Could preload items into menuItemsContainer here if you want
    }
}
