package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.Menu.CafeMenu;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import Cafe.CafeOrderSystem.Menu.MenuManagement;
import Cafe.CafeOrderSystem.utility.FxmlView;
import Cafe.CafeOrderSystem.utility.LoadFXML;
import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// WORK ON IT
public class CustomerUiController {
    private Cafe cafeShop;
    @FXML private Button checkoutButton;
    @FXML private Button logoutButton;
    @FXML private ListView<String> beverageListView;
    @FXML private ListView<PastriesItem> pastriesListView;



    private Stage primaryStage;

    public void setFacade(Cafe cafeShop) {
        this.cafeShop = cafeShop;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            beverageListView.setCellFactory(lv -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item);
                }
            });

            // Display beverages and pastries when the UI is initialized
            displayBeverages();
        });
    }


    @FXML
    private void handleListClick(MouseEvent event) {

        if (event.getClickCount() == 2) { // double-click

            String selected = beverageListView.getSelectionModel().getSelectedItem();

            if (selected != null) {

                System.out.println("Double-Clicked: " + selected);

            }

        }

    }




    public void displayBeverages() {
        // Get the list of beverage items from the cafe menu management
        MenuManagement menuManagement = cafeShop.getCafeMenuManagement();

        List<BeverageItem> observableBeverages = menuManagement.getBeverageItems();

        for (BeverageItem beverage : observableBeverages) {
            beverageListView.getItems().add(beverage.getShortSummary());
        }

    }


    // ADD: Validate Function for Menu Item if Out of Order

    @FXML
    private void handleLogOut() throws IOException {

        try {
            new LoadFXML(
                    cafeShop,    // Your Cafe facade instance
                    primaryStage,     // pass existing stage
                    FxmlView.HELLO,   //access enum
                    800,            // Width
                    600             // Height
            ).load();
        } catch (IOException e) {
            // Handle error (show dialog, log, etc.)
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCheckout() {
        System.out.println("Checkout clicked!");
    }

}
