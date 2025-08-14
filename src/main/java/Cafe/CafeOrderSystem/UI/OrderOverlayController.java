package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.Exceptions.OrderStatusChangeException;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;
import Cafe.CafeOrderSystem.Orders.OrderStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OrderOverlayController {
    @FXML
    private Label orderIdLabel;
    @FXML
    private VBox orderItemsContainer;
    @FXML
    private Label orderTotalLabel;

    private Cafe cafeShop;

    private ShowErrorDialog dialog = new ShowErrorDialog();

    private Stage stage;
    private CustomerOrder order;
    private Runnable refreshCallback;

    // Change from initialize() to setData() or similar
// In OrderOverlayController.java
    public void setFacade(Cafe cafeShop) {
        this.cafeShop = cafeShop;
    }

    // Change from initialize() to setData() or similar
    public void setOrderData(CustomerOrder order, Stage stage, Runnable refreshCallback) {
        this.order = order;
        this.stage = stage;
        this.refreshCallback = refreshCallback;

        // Populate UI
        orderIdLabel.setText("Order #" + order.getOrderID());
        orderItemsContainer.getChildren().clear();
        order.getOrderItems().forEach(item -> {
            Label itemLabel = new Label(
                    String.format("%s x%d - $%.2f",
                            item.getItem().getItemName(),
                            item.getQuantity(),
                            item.getItem().getPrice() * item.getQuantity())
            );
            orderItemsContainer.getChildren().add(itemLabel);
        });
        orderTotalLabel.setText(String.format("Total: $%.2f", order.getTotalPrice()));
    }

    @FXML
    private void handleMarkReady() {
        try {
            if (order == null) {
                throw new NullPointerException("No order selected.");
            }

            System.out.println("Current status: " + order.getOrderStatus());
            System.out.println("Trying to change to: READY");

            // Attempt to change order status through OrdersManagement
            cafeShop.getOrdersManagement().updateOrderStatus(order.getOrderID(), OrderStatus.READY);

            // Refresh UI list
            if (refreshCallback != null) {
                refreshCallback.run();
            }

            stage.close();
        } catch (OrderStatusChangeException e) {
            dialog.show("Status Change Error", e.getMessage(), e);
        } catch (NullPointerException e) {
            dialog.show("Null Pointer", "Unexpected error: " + e.getMessage(), e);
        }
    }

    @FXML
    private void handleClose() {
        stage.close();
    }
}