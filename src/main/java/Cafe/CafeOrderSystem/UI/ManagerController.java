package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.Inventory.Inventory;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientList;
import Cafe.CafeOrderSystem.Menu.MenuManagement;
import Cafe.CafeOrderSystem.Menu.Items.BeverageCost;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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
    @FXML private int currentUnits;
    @FXML private Button fulfilledOrdersButton;
    @FXML private Button addNewItemButton;
    @FXML private Button outButton;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox menuItemsContainer;
    

    @FXML
    private Button viewMenuItem;
    @FXML
    private Button viewIndgredientItem;

    private IngredientItem selectedIngredient;


    private Map<String, Map<BeverageSize, BeverageCost>> beverageDisplayMap = new HashMap<>();
    private Map<String, Double> pastryDisplayMap = new HashMap<>();

    private Stage primaryStage;
    private MenuManagement menuManagement;
    private Inventory inventoryManagement;

    

    public void setFacade(Cafe cafeShop) {
        this.cafeShop = cafeShop;
        this.menuManagement = cafeShop.getCafeMenuManagement(); 
        this.inventoryManagement = cafeShop.getInventoryManagement();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void handleMenuItems() {
        // Add logic here if needed


    viewMenuItem.setDisable(false);
    viewIndgredientItem.setDisable(true);

        
        refreshMenu();

    }


public void viewIngredientItem() {
    try {
        selectedIngredient = getSelectedIngredient(); // Implement this
        LoadFXML.loadIngredientOverlay(cafeShop, selectedIngredient, this::refreshView);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private IngredientItem getSelectedIngredient() {
    String selected = listView.getSelectionModel().getSelectedItem();
    if (selected != null) {
        // Extract ingredient name (assuming format "Name : X units")
        String ingredientName = selected.split(" : ")[0].trim();
        return inventoryManagement.getList().getIngredients().keySet().stream()
            .filter(i -> i.getIngredient().getName().equals(ingredientName))
            .findFirst()
            .orElse(null);
    }
    return null;
}



public void refreshView() {
    Platform.runLater(() -> {
        handleInventory(); // Simply refresh the inventory view
    });
    

}

private Object getSelectedMenuItem() {
    String selected = listView.getSelectionModel().getSelectedItem();
    if (selected != null) {
        // Check if it's a beverage
        for (BeverageItem item : menuManagement.getBeverageItems()) {
            if (selected.startsWith(item.name() + ":")) {
                return item;
            }
        }
        // Check if it's a pastry
        for (PastriesItem item : menuManagement.getPastriesItems()) {
            if (selected.startsWith(item.name() + " - $")) {
                return item;
            }
        }
    }
    return null;
}

@FXML
private void viewMenuItem() {
    try {
        Object selectedItem = getSelectedMenuItem();
        if (selectedItem != null) {
            LoadFXML.loadMenuOverlay(cafeShop, selectedItem, this::refreshMenu);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static void loadMenuOverlay(Cafe cafe, MenuItem menuItem, Runnable refreshCallback) throws IOException {
    LoadFXML.loadMenuOverlay(cafe, menuItem, refreshCallback);
}

private void handleIngredientSelection(String selectedItem) {
    if (selectedItem != null) {
        // Extract name from the display text (assuming format "Name : X units")
        String ingredientName = selectedItem.split(" : ")[0].trim();
        
        
        // Find the IngredientItem by name
        this.selectedIngredient = inventoryManagement.getList().getIngredients().keySet().stream()
            .filter(item -> item.getIngredient().getName().equals(ingredientName))
            .findFirst()
            .orElse(null);
    }
}
@FXML
private void handleInventory() {
    if (listView != null) {
        listView.getItems().clear();
    }
    viewMenuItem.setDisable(true);
    viewIndgredientItem.setDisable(false);
    
    IngredientList list = inventoryManagement.getList();
    Map<IngredientItem, Integer> ingredients = list.getIngredients();

    // Create a sorted list of entries
    List<Map.Entry<IngredientItem, Integer>> sortedEntries = new ArrayList<>(ingredients.entrySet());
    
    // Sort by ingredient name
    sortedEntries.sort(Comparator.comparing(
        entry -> entry.getKey().getIngredient().getName()
    ));

    // Update the ListView with sorted items
    for (Map.Entry<IngredientItem, Integer> entry : sortedEntries) {
        listView.getItems().add(String.format("%s : %d units", 
            entry.getKey().getIngredient().getName(), 
            entry.getValue()));

        
    }
    
    // Set up selection listener
    listView.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldVal, newVal) -> {
            if (newVal != null) {
                handleIngredientSelection(newVal);
            }
        });
}




    public void refreshMenu() {
 Platform.runLater(() -> {
        // Store current selection
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        
        if (listView != null) {
            listView.getItems().clear();
        }

        viewMenuItem.setDisable(false);
        viewIndgredientItem.setDisable(true);

        // viewMenuItem.setVisible(true);
        // viewIndgredientItem.setVisible(false);

        ObservableList<String> items = FXCollections.observableArrayList();

        items.add("BEVERAGES");
        List<BeverageItem> beverageItems = menuManagement.getBeverageItems();
        
        // Sort beverages by name
        beverageItems.sort(Comparator.comparing(BeverageItem::name));
        
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
        List<PastriesItem> pastryItems = new ArrayList<>(menuManagement.getPastriesItems());
        
        // Explicitly sort pastries by name
        pastryItems.sort(Comparator.comparing(PastriesItem::name));
        
        for (PastriesItem pItem : pastryItems) {
            double price = pItem.cost().price();
            pastryDisplayMap.put(pItem.name(), price);
            String displayText = String.format("%s - $%.2f", pItem.name(), price);
            items.add(displayText);
        }

        listView.setItems(items);

        // Restore selection if valid
        if (selectedIndex >= 0 && selectedIndex < listView.getItems().size()) {
            listView.getSelectionModel().select(selectedIndex);
        }

    });
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
    };

    @FXML
    private void getFulfilledOrders() {
    viewMenuItem.setDisable(true);
    viewIndgredientItem.setDisable(true);

    if(listView != null){
        listView.getItems().clear();
    }
    List<CustomerOrder> orders = cafeShop.getOrderHistory();
    for(CustomerOrder order : orders){
         listView.getItems().add(order.shortSummary());
    }
   



    }

@FXML
private void handleAddNewItem() {
    try {
        LoadFXML.loadNewItemOverlay(cafeShop, () -> {
            // This callback will be executed after the new item is created
            Platform.runLater(() -> {
                refreshMenu(); // Refresh the menu display
            });
        });
    } catch (IOException e) {
        new Alert(Alert.AlertType.ERROR, "Failed to load new item form: " + e.getMessage()).show();
        e.printStackTrace();
    }
}

    private void handleNewItemCreated(Object newItem) {
    if (newItem instanceof IngredientItem) {
        // Handle ingredient creation
        cafeShop.modifyIngredient((IngredientItem) newItem, ((IngredientItem) newItem).getAmount());
        refreshView();
    } else if (newItem instanceof PastriesItem) {
        // Handle pastry creation
        menuManagement.getPastriesItems().add((PastriesItem) newItem);
        refreshMenu();
    }
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
        Platform.runLater(() -> {
        refreshMenu(); // Simply refresh the inventory view
    }); // Initialize with inventory view
    }
}