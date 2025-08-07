package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.Menu.MenuManagement;
import Cafe.CafeOrderSystem.Menu.Items.BeverageCost;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesCost;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;
import Cafe.CafeOrderSystem.utility.FxmlView;
import Cafe.CafeOrderSystem.utility.LoadFXML;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ManagerController {
    private Cafe cafeShop;

    @FXML private Label welcomeLabel;
   @FXML private ListView<String> listView;
    @FXML private Button menuItemsButton;
    @FXML private Button inventoryButton;
    @FXML private Button fulfilledOrdersButton;
    @FXML private Button addNewItemButton;
    @FXML private Button outButton;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox menuItemsContainer;

    private Map<String, Map<BeverageSize, BeverageCost>> beverageDisplayMap = new HashMap<>();
    private Map<String, Double> pastryDisplayMap = new HashMap<>();

    private Stage primaryStage;
    private MenuManagement menuManagement;

    

    public void setFacade(Cafe cafeShop) {
        this.cafeShop = cafeShop;
        this.menuManagement = cafeShop.getCafeMenuManagement(); 
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void handleMenuItems() {
        // Add logic here if needed

        refreshMenu();

    }


    @FXML
    private void viewMenuItem(){
        

    }

    public void refreshMenu() {
    Platform.runLater(() -> {
        if (listView != null) {
            listView.getItems().clear();
        }

        ObservableList<String> items = FXCollections.observableArrayList();

        items.add("BEVERAGES");
        List<BeverageItem> beverageItems = menuManagement.getBeverageItems();

        for (BeverageItem bItem : beverageItems) {
            StringBuilder displayText = new StringBuilder(bItem.name() + ":\n");

                List<BeverageSize> sortedSizes = new ArrayList<>(bItem.cost().keySet());

                List<String> sizeOrder = List.of("SMALL", "MEDIUM", "LARGE");

                sortedSizes.sort(Comparator.comparingInt(size ->
                    sizeOrder.indexOf(size.getSize().toUpperCase(Locale.ROOT))
                ));

            for (BeverageSize size : sortedSizes) {
                BeverageCost cost = bItem.cost().get(size);
                if (cost != null) {
                    displayText.append(String.format(" %s - $%.2f\n", size.getSize(), cost.price()));
                }
            }

            items.add(displayText.toString());
        }

        items.add("PASTRIES");
        List<PastriesItem> pastryItems = menuManagement.getPastriesItems();
        for (PastriesItem pItem : pastryItems) {
            double price = pItem.cost().price();
            pastryDisplayMap.put(pItem.name(), price);
            String displayText = String.format("%s - $%.2f", pItem.name(), price);
            items.add(displayText);
        }

        listView.setItems(items);

        // Bold only "BEVERAGES" and "PASTRIES"
        listView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    if (item.equals("BEVERAGES") || item.equals("PASTRIES")) {
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setStyle(""); // Reset style
                    }
                }
            }
        });
    });
}


    @FXML
    private void handleInventory() {
        Platform.runLater(() -> {
       if(listView !=null){
         listView.getItems().clear();
       }

              });
    }

    @FXML
    private void getFulfilledOrders() {

    listView.getItems().clear();
    List<CustomerOrder> orders = cafeShop.getOrderHistory();
    for(CustomerOrder order : orders){
         listView.getItems().add(order.shortSummary());
    }
   



    }

    @FXML
    private void handleAddNewItem() {
        
    }

    @FXML
    private void handleQuit() throws IOException {
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
    private void initialize() {
        welcomeLabel.setText("Welcome Manager Jane Doe");
        refreshMenu();
        // Could preload items into menuItemsContainer here if you want
    }
}
