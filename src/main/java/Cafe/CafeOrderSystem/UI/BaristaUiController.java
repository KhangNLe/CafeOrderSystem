package Cafe.CafeOrderSystem.UI;
import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;
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


public class BaristaUiController {
    private Cafe cafeShop;
    @FXML private ListView<String> orderListView;
    @FXML private Button checkoutButton;
    @FXML private Button fulfilledOrdersButton;
    @FXML private Button pendingOrdersButton;
    private boolean orderTypeToggle = false;

    private Stage primaryStage;

    public void setFacade(Cafe cafeShop){
        this.cafeShop = cafeShop;
    }
    
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
    List<CustomerOrder> pending = cafeShop.getPendingOrders();
    

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
    List<CustomerOrder> orders = cafeShop.getOrderHistory();
    for (CustomerOrder order : orders) {
        orderListView.getItems().add(order.shortSummary()); // or order.toString()
    }
}

@FXML
private void initialize() {
    orderTypeToggle = false; // Default to pending
                     // Optional: update style
    //WE WILL NEED TO REFACTOR THIS LATER BECAUSE FOR NOW, IT WILL CRASH THE UI
    //SINCE Cafe OBJECT GET PASSED TO THE CLASS AFTER THIS IS CALLED
    //getPendingOrders(); // Load immediately
}

}

