package Cafe.CafeOrderSystem.UI;
import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.Exceptions.InvalidModifyingException;
import Cafe.CafeOrderSystem.Exceptions.OrderStatusChangeException;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;
import Cafe.CafeOrderSystem.Orders.OrderStatus;
import Cafe.CafeOrderSystem.utility.FxmlView;
import Cafe.CafeOrderSystem.utility.LoadFXML;
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



public class BaristaUiController extends Controller{
    private Cafe cafeShop;
    // @FXML private ListView<String> orderListView;
    @FXML private VBox menuItemsContainer;
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
            LoadFXML.loadOrderOverlay(
                cafeShop,
                selectedOrder,
                this::refreshOrders
            );
        } catch (IOException e) {
            dialog.show("Loading Error", "Failed to load order details", e);
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
    try {
        new LoadFXML(
            cafeShop,    // Your Cafe facade instance
            primaryStage,     // pass existing stage
            FxmlView.LOGIN,   //access enum
            800,            // Width
            600             // Height
        ).load();
    } catch (IOException e) {
        // Handle error (show dialog, log, etc.)
        e.printStackTrace();
        }


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



public void initAfterInjection() {
    getPendingOrders(); // now safe to call
}

}

