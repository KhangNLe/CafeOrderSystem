package Cafe.CafeOrderSystem.UI;

import java.io.IOException;

import Cafe.CafeOrderSystem.Cafe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HelloController {
    private final double LOGIN_WIDTH = 800;
    private final double LOGIN_HEIGHT = 600;
    private Cafe cafeShop;
    
    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML private Label welcomeLabel;
    @FXML private Button customerButton;
    @FXML private Button loginButton;
    @FXML private Pane imagePlaceholder;

    @FXML

    private void handleCustomerOrder() {
        // Navigate to customer order screen
        System.out.println("Customer order button clicked");
    }
    @FXML
    private void handleLogin() throws IOException{
        // Navigate to login screen

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cafe/CafeOrderSystem/login.fxml"));
    Parent root = loader.load();
    
    // Get the controller JavaFX created
    LoginController loginController = loader.getController();
    
    // Pass the stage forward
    loginController.setPrimaryStage(primaryStage);
    
    // Show the new screen
    primaryStage.setScene(new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT));
    primaryStage.setTitle("Barista Dashboard");
        System.out.println("Employee login button clicked");
    }

    public void setFacade(Cafe cafe) {
        this.cafeShop = cafe;
    }

}
