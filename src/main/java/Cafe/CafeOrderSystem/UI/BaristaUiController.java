package Cafe.CafeOrderSystem.UI;
import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.Exceptions.InvalidModifyingException;
import Cafe.CafeOrderSystem.Exceptions.OrderStatusChangeException;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;
import Cafe.CafeOrderSystem.Orders.OrderStatus;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.List;
import java.util.Queue;



public class BaristaUiController {
    private Cafe cafeShop;
    // @FXML private ListView<String> orderListView;
    @FXML private ListView<CustomerOrder> orderListView;
    @FXML private Button checkoutButton;
    @FXML private Button fulfilledOrdersButton;
    @FXML private Button pendingOrdersButton;
    @FXML private VBox overlayPane;
    @FXML private Label orderIdLabel;
    @FXML private VBox orderItemsContainer;
    @FXML private Label orderTotalLabel;
    @FXML private Pane overlayBackground;


    private CustomerOrder selectedOrder;

    private boolean orderTypeToggle = false;

    private Stage primaryStage;

    private ShowErrorDialog dialog = new ShowErrorDialog();
    
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


// private void showOrderDetails(CustomerOrder order) {
//     // 1. Update order details
//     orderIdLabel.setText("Order #" + order.getOrderID());
    
//     // 2. Clear and rebuild items list
//     orderItemsContainer.getChildren().clear();
//     order.getOrderItems().forEach(item -> {
//         HBox itemBox = new HBox(10);
//         Label nameLabel = new Label(item.getItem().getItemName());
//         Label qtyLabel = new Label("x" + item.getQuantity());
//         Label priceLabel = new Label(String.format("$%.2f", 
//                                item.getItem().getPrice() * item.getQuantity()));
//         itemBox.getChildren().addAll(nameLabel, qtyLabel, priceLabel);
//         orderItemsContainer.getChildren().add(itemBox);
//     });
    
//     // 3. Update total
//     orderTotalLabel.setText(String.format("Total: $%.2f", order.getTotalPrice()));
    
//     // 4. Position overlay at center
//     overlayPane.setLayoutX((overlayBackground.getWidth() - overlayPane.getWidth()) / 2);
//     overlayPane.setLayoutY((overlayBackground.getHeight() - overlayPane.getHeight()) / 2);
    
//     // 5. Make visible (background first, then content)
//     overlayBackground.setVisible(true);
//     overlayPane.setVisible(true);
// }
@FXML
private void closeOverlay() {
    overlayBackground.setVisible(false);
    overlayPane.setVisible(false);
}

@FXML
private void handleMarkReady() {
    try {
        cafeShop.getOrdersManagement()
            .updateOrderStatus(selectedOrder.getOrderID(), OrderStatus.READY);
        closeOverlay();
        getFufilledOrders();
        getPendingOrders();
    } catch (InvalidModifyingException e) {
        dialog.show("Error", "Failed to update order status", e);
    }
}
@FXML
private void selectOrder() {
    if (selectedOrder != null) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Cafe/CafeOrderSystem/order-overlay.fxml"));
            Parent root = loader.load();
            
            Stage overlayStage = new Stage();
            overlayStage.initModality(Modality.APPLICATION_MODAL);
            overlayStage.initStyle(StageStyle.UNDECORATED);
            overlayStage.setScene(new Scene(root));
            
            OrderOverlayController controller = loader.getController();

            controller.setFacade(cafeShop);
            // Call the renamed method:
            controller.setOrderData(selectedOrder, overlayStage, this::refreshOrders);
            
            overlayStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error
        }
    }
}

    public void refreshOrders() {
    Platform.runLater(() -> {
        orderListView.getItems().clear();
        List<CustomerOrder> orders = cafeShop.getPendingOrders();
        orderListView.getItems().addAll(orders);
    });
}


    @FXML
    private void handleCompleteOrder(){
        System.out.println("complete order clicked");

        System.out.println(selectedOrder);
        try {
        if (selectedOrder == null) {
            throw new NullPointerException("No order selected.");
        }
        selectedOrder.changeOrderStatus(OrderStatus.READY);
        cafeShop.getOrdersManagement().fulfilledOrder(selectedOrder);
        if (selectedOrder.getOrderStatus() != OrderStatus.READY) {
            throw new OrderStatusChangeException("Could not change status for order ID: " + selectedOrder.getOrderID());
        }

        getPendingOrders(); // Refresh list
    } catch (OrderStatusChangeException e) {
        dialog.show("Status Change Error", e.getMessage(), e);
    } catch (NullPointerException e) {
        dialog.show("Null Pointer","Unexpected error: " + e.getMessage(), e);
    }

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
        orderListView.getItems().clear();
        // Optionally: show a placeholder label instead of injecting strings into a typed list
        return;
    }


        orderListView.getItems().clear();
        orderListView.getItems().addAll(pending); // Add full CustomerOrder objects
}





@FXML
private void getFufilledOrders() {
    if (orderTypeToggle) return;

    orderTypeToggle = true;
    fulfilledOrdersButton.setStyle("-fx-background-color: lightgreen; -fx-text-fill: black;");
    pendingOrdersButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");

    orderListView.getItems().clear();
    List<CustomerOrder> orders = cafeShop.getOrderHistory();
    orderListView.getItems().addAll(orders);
}

@FXML
public void initialize() {
    orderListView.setCellFactory(param -> new ListCell<>() {
        @Override
        protected void updateItem(CustomerOrder item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.shortSummary());
        }
    });

    // Simplified selection listener - just stores selection
    orderListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        selectedOrder = newVal;
    });
}

// @FXML
// public void initialize() {
//     orderListView.setCellFactory(param -> new ListCell<>() {
//         @Override
//         protected void updateItem(CustomerOrder item, boolean empty) {
//             super.updateItem(item, empty);
//             setText(empty || item == null ? null : item.shortSummary());
//         }
//     });

// orderListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
//     selectedOrder = newVal;
// if (selectedOrder != null && selectedOrder.getOrderStatus() == OrderStatus.PENDING) {
//             try {
//                 // Use the management system to properly update the status
//                 boolean success = cafeShop.getOrdersManagement()
//                     .updateOrderStatus(selectedOrder.getOrderID(), OrderStatus.IN_PROCESS);
                
//                 if (!success) {
//                     throw new InvalidModifyingException("Failed to update order status");
//                 }
                
//                 // Refresh the list view
//                 getPendingOrders();
                
//             } catch (InvalidModifyingException e) {
//                 dialog.show("Status Change Error", e.getMessage(), e);
//             }
//         }
//     });
// }


public void initAfterInjection() {
    getPendingOrders(); // now safe to call
}

}

