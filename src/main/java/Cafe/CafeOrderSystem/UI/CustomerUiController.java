package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import Cafe.CafeOrderSystem.Menu.MenuManagement;
import Cafe.CafeOrderSystem.utility.FxmlView;
import Cafe.CafeOrderSystem.utility.LoadFXML;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import Cafe.CafeOrderSystem.Menu.Items.CustomItem;

public class CustomerUiController {
    private Cafe cafeShop;
    private Stage primaryStage;

    @FXML private Button checkoutButton;
    @FXML private Button logoutButton;
    @FXML private ListView<String> beverageListView;
    @FXML private ListView<String> cartListView;

    private ObservableList<String> cartItems = FXCollections.observableArrayList();
    private List<Object> cartItemObjects = new ArrayList<>(); // Stores actual items
    private List<BeverageSize> cartItemSizes = new ArrayList<>(); // For beverages
    private List<Integer> cartItemQuantities = new ArrayList<>();
    private double cartTotal = 0.0;


    public void setFacade(Cafe cafeShop) {
        this.cafeShop = cafeShop;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            beverageListView.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item);
                }
            });

            // Set up cart list
            cartListView.setItems(cartItems);

            // Load initial data
            Platform.runLater(this::displayBeveragesWithCustomizations);
        });
    }


    private void displayBeveragesWithCustomizations() {
        MenuManagement menuManagement = cafeShop.getCafeMenuManagement();
        Map<BeverageItem, List<CustomItem>> beveragesWithCustomizations =
                menuManagement.getBeverageWithCustomizeOption();

        for (BeverageItem beverage : beveragesWithCustomizations.keySet()) {
            beverageListView.getItems().add(beverage.getShortSummary());
        }
    }



    @FXML
    private void handleListClick(MouseEvent event) {

        if (event.getClickCount() == 2) { // double-click

            String selectedSummary = beverageListView.getSelectionModel().getSelectedItem();

            if (selectedSummary != null) {

                System.out.println("Double-Clicked: " + selectedSummary);

                MenuManagement menuManagement = cafeShop.getCafeMenuManagement();
                Map<BeverageItem, List<CustomItem>> beveragesWithCustomizations =
                        menuManagement.getBeverageWithCustomizeOption();

                beveragesWithCustomizations.keySet().stream()
                        .filter(b -> b.getShortSummary().equals(selectedSummary))
                        .findFirst().ifPresent(selectedBeverage -> handleBeverageSelection(selectedBeverage,
                                beveragesWithCustomizations.get(selectedBeverage)));


            }

        }

    }


    private void handleBeverageSelection(BeverageItem beverage, List<CustomItem> customizations) {
        // TODO: Validate Function for Menu Item if Out of Order

        // Step 1: Select size
        BeverageSize size = showSizeSelectionDialog(beverage);
        if (size == null) return;

        // Step 2: Select customizations
        List<CustomItem> selectedCustomizations = showCustomizationDialog(customizations);

        // Step 3: Add to cart
        addBeverageToCart(beverage, size, selectedCustomizations);
    }



    private BeverageSize showSizeSelectionDialog(BeverageItem beverage) {
        List<BeverageSize> availableSizes = new ArrayList<>(beverage.cost().keySet());
        ChoiceDialog<BeverageSize> dialog = new ChoiceDialog<>(availableSizes.getFirst(), availableSizes);

        dialog.setTitle("Select Size");
        dialog.setHeaderText("Select size for " + beverage.name());
        dialog.setContentText("Choose size:");

        return dialog.showAndWait().orElse(null);
    }

    private List<CustomItem> showCustomizationDialog(List<CustomItem> availableCustomizations) {
        if (availableCustomizations.isEmpty()) {
            return new ArrayList<>();
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Customize Your Drink");
        alert.setHeaderText("Available Customizations");

        // Create checkboxes for each customization
        List<CheckBox> checkBoxes = availableCustomizations.stream()
                .map(c -> new CheckBox(c.name() + " (+$" + c.additionalPrice() + ")"))
                .toList();

        VBox content = new VBox(10);
        content.getChildren().addAll(checkBoxes);
        alert.getDialogPane().setContent(content);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            List<CustomItem> selected = new ArrayList<>();
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isSelected()) {
                    selected.add(availableCustomizations.get(i));
                }
            }
            return selected;
        }
        return new ArrayList<>();
    }

    private void addBeverageToCart(BeverageItem beverage, BeverageSize size,
                                   List<CustomItem> customizations) {
        // Calculate base price
        double basePrice = beverage.cost().get(size).price();

        // Calculate customization additions
        double customizationTotal = customizations.stream()
                .mapToDouble(CustomItem::additionalPrice)
                .sum();

        // Create display string
        StringBuilder display = new StringBuilder();
        display.append("1 x ").append(beverage.name()).append(" (").append(size).append(")");

        if (!customizations.isEmpty()) {
            display.append(" with ");
            display.append(customizations.stream()
                    .map(c -> c.name() + " (+$" + c.additionalPrice() + ")")
                    .collect(Collectors.joining(", ")));
        }

        display.append(" - $").append(String.format("%.2f", basePrice + customizationTotal));

        // Add to cart
        cartItems.add(display.toString());
        cartTotal += basePrice + customizationTotal;
        updateCartDisplay();
    }


    private void updateCartDisplay() {

        for (int i = 0; i < cartItemObjects.size(); i++) {
            Object item = cartItemObjects.get(i);
            int quantity = cartItemQuantities.get(i);
            double price;
            String displayName;

            if (item instanceof BeverageItem beverage) {
                BeverageSize size = cartItemSizes.get(i);
                price = beverage.cost().get(size).price();
                displayName = beverage.name() + " (" + size + ")";
            } else {
                PastriesItem pastry = (PastriesItem) item;
                price = pastry.cost().price();
                displayName = pastry.name();
            }

            double itemTotal = price * quantity;
            cartTotal += itemTotal;

            cartItems.add(String.format("%d x %s - $%.2f",
                    quantity, displayName, itemTotal));
        }

        checkoutButton.setText(String.format("Checkout ($%.2f)", cartTotal));
    }


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
