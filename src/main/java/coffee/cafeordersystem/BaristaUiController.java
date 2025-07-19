package coffee.cafeordersystem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;



public class BaristaUiController {
    @FXML private ListView<String> menuListView;
    @FXML private Button checkoutButton;

    private Stage primaryStage;
    
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void handleLogOut() throws IOException {
        // Return to login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        
    // Get existing scene and just replace the root
    Scene currentScene = primaryStage.getScene();
    currentScene.setRoot(root);
    
    // Optional: Reset window size
    primaryStage.setWidth(800);
    primaryStage.setHeight(600);
    }

    @FXML
    private void handleCheckout() {
        System.out.println("Checkout clicked!");
    }

    @FXML
    private void initialize() {
        menuListView.getItems().addAll("Latte", "Cappuccino", "Croissant");
    }


}

