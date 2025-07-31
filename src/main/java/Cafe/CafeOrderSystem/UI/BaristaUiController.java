package Cafe.CafeOrderSystem.UI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

import Cafe.CafeOrderSystem.JsonParser.OrderItem.OrderHistoryParser;
import Cafe.CafeOrderSystem.JsonParser.OrderItem.PendingOrderParser;
import Cafe.CafeOrderSystem.Orders.CafeOrders;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;



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
    private void handleQuit() throws IOException{
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
private void getPendingOrders() {
    orderTypeToggle = false;
    

    fulfilledOrdersButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
    pendingOrdersButton.setStyle("-fx-background-color: lightgreen; -fx-text-fill: black;");

    orderListView.getItems().clear();
    PendingOrderParser.getPendingOrders(); // Updates CafeOrders singleton
    List<CustomerOrder> pending = CafeOrders.getInstance().getPendingOrders();
    

    if (pending == null || pending.isEmpty()) {
        orderListView.getItems().add("All Done :)");
        return;
    }

    for (CustomerOrder order : pending) {
        orderListView.getItems().add(order.shortSummary());
    }
}





@FXML
private void getFufilledOrders() {

            if(orderTypeToggle){
            return;
        }
        orderTypeToggle = true;
        fulfilledOrdersButton.setStyle("-fx-background-color: lightgreen; -fx-text-fill: black;");
        pendingOrdersButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

        orderListView.getItems().clear();
    List<CustomerOrder> orders = OrderHistoryParser.loadOrderHistory();
    for (CustomerOrder order : orders) {
        orderListView.getItems().add(order.shortSummary()); // or order.toString()
    }
}

@FXML
private void initialize() {
    orderTypeToggle = false; // Default to pending
                     // Optional: update style
    getPendingOrders(); // Load immediately
}

}

