package Cafe.CafeOrderSystem.UI;

import java.io.IOException;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.utility.FxmlView;
import Cafe.CafeOrderSystem.utility.LoadFXML;
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

    public void setFacade(Cafe cafeShop){
        this.cafeShop = cafeShop;
    }

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
        private void openCustomer() throws IOException {
        try {
        new LoadFXML(
            cafeShop,    // Your Cafe facade instance
            primaryStage,     // pass existing stage
            FxmlView.CUSTOMER,   //access enum
            800,            // Width
            600             // Height
        ).load();
    } catch (IOException e) {
        // Handle error (show dialog, log, etc.)
        e.printStackTrace();
        }


    }
    @FXML
    private void handleLogin() throws IOException{
        // Navigate to login screen

    // FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cafe/CafeOrderSystem/login.fxml"));
    // Parent root = loader.load();
    
    // // Get the controller JavaFX created
    // LoginController loginController = loader.getController();
    // loginController.setFacade(cafeShop);
    
    // // Pass the stage forward
    // loginController.setPrimaryStage(primaryStage);
    
    // // Show the new screen
    // primaryStage.setScene(new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT));
    // primaryStage.setTitle("Barista Dashboard");

        new LoadFXML(
            cafeShop,    // Your Cafe facade instance
            primaryStage,     // Or pass existing stage
            FxmlView.LOGIN,
            800,            // Width
            600             // Height
        ).load();
        System.out.println("Employee login button clicked");
    }
}
