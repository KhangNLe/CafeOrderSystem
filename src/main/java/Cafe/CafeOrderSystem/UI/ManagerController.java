package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;


public class ManagerController {
    private Cafe cafeShop;

    @FXML private Label welcomeLabel;
    @FXML private Button menuItemsButton;
    @FXML private Button inventoryButton;
    @FXML private Button fulfilledOrdersButton;
    @FXML private Button addNewItemButton;
    @FXML private Button outButton;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox menuItemsContainer;


    private Stage primaryStage;

    public void setFacade(Cafe cafeShop) {
        this.cafeShop = cafeShop;
    }

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
    private void handleQuit() throws IOException {
        // Close or log out
        // Load login screen first
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cafe/CafeOrderSystem/login.fxml"));
        Parent root = loader.load();

        // Pass stage to login controller
        LoginController loginController = loader.getController();
        loginController.setPrimaryStage(primaryStage);

        Scene scene = new Scene(root, 800, 600); // Initial size
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void initialize() {
        welcomeLabel.setText("Welcome Manager Jane Doe");
        // Could preload items into menuItemsContainer here if you want
    }
}
