package Cafe.CafeOrderSystem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;



public class BaristaUiController {
    @FXML private ListView<String> orderListView;
    @FXML private Button checkoutButton;
    @FXML private Button fulfilledOrdersButton;
    @FXML private Button pendingOrdersButton;
    private boolean orderTypeToggle = false;

    private Stage primaryStage;
    
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    // @FXML
    // private void handleLogOut() throws IOException {
    //     // Return to login screen
    //     FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
    //     Parent root = loader.load();
        
    // // Get existing scene and just replace the root
    // Scene currentScene = primaryStage.getScene();
    // currentScene.setRoot(root);
    
    // // Optional: Reset window size
    // primaryStage.setWidth(800);
    // primaryStage.setHeight(600);
    // }

    @FXML
    private void handleChangeStatus() {
        
    }

    @FXML
    private void handleCompleteOrder(){

    }
    @FXML
    private void handleQuit(){

    }

    @FXML
    private void getFufilledOrders(){
        if(orderTypeToggle){
            return;
        }
        orderTypeToggle = true;
        fulfilledOrdersButton.setStyle("-fx-background-color: lightgreen; -fx-text-fill: black;");
        pendingOrdersButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

    }


    @FXML
    private void getPendingOrders(){
        if(!orderTypeToggle){
            return;
        }
        orderTypeToggle = false;
        fulfilledOrdersButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
        pendingOrdersButton.setStyle("-fx-background-color: lightgreen; -fx-text-fill: black;");

    }


    // @FXML
    // private void initialize() {
    //     orderListView.getItems().addAll("Latte", "Cappuccino", "Croissant"); //customer order.getNextPendingOrder() 
    // }


}

