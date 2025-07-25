package Cafe.CafeOrderSystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    private final double LOGIN_WIDTH = 800;
    private final double LOGIN_HEIGHT = 600;
    
    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Barista", "Customer", "Manager");
        roleComboBox.setValue("Customer");
    }

    @FXML
    private void handleLogin() throws IOException {
        String role = roleComboBox.getValue();
        
        switch (role) {
            case "Barista" -> openBaristaScreen();
            case "Customer" -> openCustomerScreen();
            case "Manager" -> openManagerScreen();
        }
    }

    private void openBaristaScreen() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("barista-view.fxml"));
    Parent root = loader.load();
    
    // Get the controller JavaFX created
    BaristaUiController baristaController = loader.getController();
    
    // Pass the stage forward
    baristaController.setPrimaryStage(primaryStage);
    
    // Show the new screen
    primaryStage.setScene(new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT));
    primaryStage.setTitle("Barista Dashboard");
    }



    private void openCustomerScreen() throws IOException {
        // Similar implementation
    }

    private void openManagerScreen() throws IOException {
        // Similar implementation
    }

    private void showScene(Parent root, String title) {
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}