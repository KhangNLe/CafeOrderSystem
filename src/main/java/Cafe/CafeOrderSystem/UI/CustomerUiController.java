package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Inventory.Inventory;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import Cafe.CafeOrderSystem.Menu.MenuManagement;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;
import Cafe.CafeOrderSystem.Orders.OrderItem;
import Cafe.CafeOrderSystem.Orders.OrderStatus;
import Cafe.CafeOrderSystem.Orders.OrdersManagement;
import Cafe.CafeOrderSystem.utility.FxmlView;
import Cafe.CafeOrderSystem.utility.LoadFXML;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import Cafe.CafeOrderSystem.Menu.Items.CustomItem;

public class CustomerUiController {
    private Cafe cafeShop;
    private Stage primaryStage;

    @FXML private Button checkoutButton;
    @FXML private Button logoutButton;
    @FXML private Button clearOrderButton;
    @FXML private ListView<String> beverageListView;
    @FXML private ListView<String> pastriesListView;
    @FXML private ListView<String> cartListView;
    @FXML private TextField customerNameField;
    @FXML private Pane customizationOverlay;
    @FXML private VBox customizationPane;
    @FXML private VBox customizationOptionsContainer;
    @FXML private Label customizationBeverageName;
    @FXML private Label customizationTotalLabel;

    private BeverageItem currentBeverage;
    private BeverageSize currentSize;
    private List<CustomItem> selectedCustomizations = new ArrayList<>();
    private double currentCustomizationPrice;
    private List<List<CustomItem>> cartItemCustomizations = new ArrayList<>();

    private ObservableList<String> cartItems = FXCollections.observableArrayList();
    private List<Object> cartItemObjects = new ArrayList<>(); // Stores actual items
    private List<BeverageSize> cartItemSizes = new ArrayList<>(); // For beverages
    private List<Integer> cartItemQuantities = new ArrayList<>();
    private double cartTotal = 0.0;
    private String currentOrderId;

    private Inventory inventory;


    public void setFacade(Cafe cafeShop) {
        this.cafeShop = cafeShop;
        this.inventory = cafeShop.getInventoryManagment();
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

            pastriesListView.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item);
                }
            });

            // Set up cart list
            cartListView.setItems(cartItems);

            // Load initial data
            Platform.runLater(() -> {
                displayBeveragesWithCustomizations();
                displayPastries();
            });
        });
    }

    private void displayPastries() {
        MenuManagement menuManagement = cafeShop.getCafeMenuManagement();
        List<PastriesItem> pastries = menuManagement.getPastriesItems();

        pastriesListView.getItems().clear();
        for (PastriesItem pastry : pastries) {
            String summary = pastry.getShortSummary();
            if (!isPastryAvailable(pastry)) {
                summary += " (Out of Stock)";
            }
            pastriesListView.getItems().add(summary);
        }
    }


    private void displayBeveragesWithCustomizations() {
        MenuManagement menuManagement = cafeShop.getCafeMenuManagement();
        Map<BeverageItem, List<CustomItem>> beveragesWithCustomizations =
                menuManagement.getBeverageWithCustomizeOption();

        beverageListView.getItems().clear();
        for (BeverageItem beverage : beveragesWithCustomizations.keySet()) {
            String summary = beverage.getShortSummary();
            boolean available = isBeverageAvailable(beverage, beverage.cost().keySet().iterator().next(),
                    new ArrayList<>());

            if (!available) {
                summary += " (Out of Stock)";
            }
            beverageListView.getItems().add(summary);
        }
    }



    @FXML
    private void handleBeverageListClick(MouseEvent event) {

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
        this.currentBeverage = beverage;

        System.out.println("Selected beverage: " + beverage.name());
        System.out.println("Available customizations: " + customizations.size());

        // Step 1: Select size
        BeverageSize size = showSizeSelectionDialog(beverage);
        if (size == null) return;
        this.currentSize = size;

        if (!isBeverageAvailable(beverage, size, customizations)) {
            showAlert("Out of Stock",
                    "Sorry, we don't have enough ingredients for " + beverage.name() +
                            " (" + size + "). Please choose another item.");
            return;
        }

        // Step 2: Show customization dialog if available
        if (!customizations.isEmpty()) {
            System.out.println("Showing customization dialog with " + customizations.size() + " options");
            showCustomizationDialog(beverage, size, customizations);
        } else {
            System.out.println("No customizations available for this beverage");
            addBeverageToCart(beverage, size, new ArrayList<>());
        }
    }



    private BeverageSize showSizeSelectionDialog(BeverageItem beverage) {
        List<BeverageSize> availableSizes = new ArrayList<>(beverage.cost().keySet());
        ChoiceDialog<BeverageSize> dialog = new ChoiceDialog<>(availableSizes.getFirst(), availableSizes);

        dialog.setTitle("Select Size");
        dialog.setHeaderText("Select size for " + beverage.name());
        dialog.setContentText("Choose size:");

        return dialog.showAndWait().orElse(null);
    }

    private List<CustomItem> getCustomizationsForItem(int index) {
        return index < cartItemCustomizations.size() ?
                cartItemCustomizations.get(index) :
                new ArrayList<>();
    }

    private void showCustomizationDialog(BeverageItem beverage, BeverageSize size,
                                         List<CustomItem> availableCustomizations) {
        customizationBeverageName.setText(beverage.name() + " (" + size + ")");
        customizationOptionsContainer.getChildren().clear();
        selectedCustomizations.clear();

        // Calculate base price
        double basePrice = beverage.cost().get(size).price();
        currentCustomizationPrice = basePrice;

        // Create checkboxes for each customization
        for (CustomItem custom : availableCustomizations) {
            CheckBox checkBox = new CheckBox(custom.name() + " (+$" + custom.additionalPrice() + ")");
            checkBox.setUserData(custom);
            checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    selectedCustomizations.add(custom);
                    currentCustomizationPrice += custom.additionalPrice();
                } else {
                    selectedCustomizations.remove(custom);
                    currentCustomizationPrice -= custom.additionalPrice();
                }
                customizationTotalLabel.setText(String.format("Total: $%.2f", currentCustomizationPrice));
            });
            customizationOptionsContainer.getChildren().add(checkBox);
        }

        customizationTotalLabel.setText(String.format("Total: $%.2f", currentCustomizationPrice));
        customizationOverlay.setVisible(true);
        customizationPane.setVisible(true);
    }

    @FXML
    private void handlePastryListClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // double-click
            String selectedSummary = pastriesListView.getSelectionModel().getSelectedItem();

            if (selectedSummary != null) {
                MenuManagement menuManagement = cafeShop.getCafeMenuManagement();
                List<PastriesItem> pastries = menuManagement.getPastriesItems();

                pastries.stream()
                        .filter(p -> p.getShortSummary().equals(selectedSummary))
                        .findFirst()
                        .ifPresent(this::handlePastrySelection);
            }
        }
    }

    @FXML
    private void handleAddCustomizedToCart() {
        if (currentBeverage != null && currentSize != null) {
            addBeverageToCart(currentBeverage, currentSize, selectedCustomizations);
            closeCustomizationOverlay();
        }
    }

    @FXML
    private void closeCustomizationOverlay() {
        customizationOverlay.setVisible(false);
        customizationPane.setVisible(false);
    }

    private void handlePastrySelection(PastriesItem pastry) {
        if (!isPastryAvailable(pastry)) {
            showAlert("Out of Stock",
                    "Sorry, we don't have enough ingredients for " + pastry.name() +
                            ". Please choose another item.");
            return;
        }
        addPastryToCart(pastry);
    }

    private void addPastryToCart(PastriesItem pastry) {
        // Create display string
        String display = "1 x " + pastry.name() + " - $" + String.format("%.2f", pastry.cost().price());

        // Add to ALL cart tracking lists
        cartItemObjects.add(pastry);
        cartItemSizes.add(null); // Add null for size since pastries don't have sizes
        cartItemQuantities.add(1); // Default quantity of 1
        cartTotal += pastry.cost().price();
        updateCartDisplay();
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

        cartItemCustomizations.add(new ArrayList<>(customizations));

        display.append(" - $").append(String.format("%.2f", basePrice + customizationTotal));

        // Add to cart tracking lists
        cartItemObjects.add(beverage);
        cartItemSizes.add(size);
        cartItemQuantities.add(1);
        cartTotal += basePrice + customizationTotal;
        updateCartDisplay();
    }


    private boolean isBeverageAvailable(BeverageItem beverage, BeverageSize size,
                                        List<CustomItem> customizations) {
        // Get base ingredients for the size
        Map<Ingredients, Integer> requiredIngredients = new HashMap<>(
                beverage.cost().get(size).ingredients()
        );

        // Add customization ingredients
        for (CustomItem custom : customizations) {
            if (custom.ingredients() != null) {
                custom.ingredients().forEach((ingredient, amount) ->
                        requiredIngredients.merge(ingredient, amount, Integer::sum)
                );
            }
        }

        // Check each ingredient
        for (Map.Entry<Ingredients, Integer> entry : requiredIngredients.entrySet()) {
            IngredientItem stockItem = findIngredientItem(entry.getKey());
            if (stockItem == null || stockItem.getAmount() < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    private boolean isPastryAvailable(PastriesItem pastry) {
        Map<Ingredients, Integer> requiredIngredients = pastry.cost().ingredients();

        for (Map.Entry<Ingredients, Integer> entry : requiredIngredients.entrySet()) {
            IngredientItem stockItem = findIngredientItem(entry.getKey());
            if (stockItem == null || stockItem.getAmount() < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    private IngredientItem findIngredientItem(Ingredients ingredient) {
        try {
            for (int i = 0; ; i++) {
                IngredientItem item = inventory.getList().getObject(i);
                if (item.getIngredient().equals(ingredient)) {
                    return item;
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    private boolean deductOrderIngredients() {
        for (int i = 0; i < cartItemObjects.size(); i++) {
            Object item = cartItemObjects.get(i);
            int quantity = cartItemQuantities.get(i);

            if (item instanceof BeverageItem beverage) {
                BeverageSize size = cartItemSizes.get(i);
                List<CustomItem> customizations = getCustomizationsForItem(i);

                if (!deductBeverageIngredients(beverage, size, customizations, quantity)) {
                    return false;
                }
            }
            else if (item instanceof PastriesItem pastry) {
                if (!deductPastryIngredients(pastry, quantity)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean deductBeverageIngredients(BeverageItem beverage, BeverageSize size,
                                              List<CustomItem> customizations, int quantity) {
        // Get base ingredients
        Map<Ingredients, Integer> ingredients = beverage.cost().get(size).ingredients();

        // Add customization ingredients
        for (CustomItem custom : customizations) {
            if (custom.ingredients() != null) {
                custom.ingredients().forEach((ingr, amt) ->
                        ingredients.merge(ingr, amt, Integer::sum)
                );
            }
        }

        // Multiply by quantity and deduct
        for (Map.Entry<Ingredients, Integer> entry : ingredients.entrySet()) {
            IngredientItem stockItem = findIngredientItem(entry.getKey());
            if (stockItem == null) {
                return false;
            }

            int amountNeeded = entry.getValue() * quantity;
            int currentAmount = stockItem.getAmount();

            if (currentAmount < amountNeeded) {
                return false;
            }

            // Subtract the amount
            if (!inventory.modifyInventory(-amountNeeded, stockItem)) {
                return false;
            }
        }
        return true;
    }

    private boolean deductPastryIngredients(PastriesItem pastry, int quantity) {
        Map<Ingredients, Integer> ingredients = pastry.cost().ingredients();

        for (Map.Entry<Ingredients, Integer> entry : ingredients.entrySet()) {
            IngredientItem stockItem = findIngredientItem(entry.getKey());
            if (stockItem == null) {
                return false;
            }

            int amountNeeded = entry.getValue() * quantity;
            int currentAmount = stockItem.getAmount();

            if (currentAmount < amountNeeded) {
                return false;
            }

            // Subtract the amount
            if (!inventory.modifyInventory(-amountNeeded, stockItem)) {
                return false;
            }
        }
        return true;
    }


    private void updateCartDisplay() {
        cartItems.clear();
        cartTotal = 0.0;

        for (int i = 0; i < cartItemObjects.size(); i++) {
            Object item = cartItemObjects.get(i);
            int quantity = cartItemQuantities.get(i);
            double price;
            String displayName;

            if (item instanceof BeverageItem beverage) {
                BeverageSize size = cartItemSizes.get(i);
                price = beverage.cost().get(size).price();
                displayName = beverage.name() + " (" + size + ")";

                // Add customizations to display
                List<CustomItem> customizations = getCustomizationsForItem(i);
                if (!customizations.isEmpty()) {
                    displayName += " with " + customizations.stream()
                            .map(c -> c.name())
                            .collect(Collectors.joining(", "));
                    price += customizations.stream()
                            .mapToDouble(CustomItem::additionalPrice)
                            .sum();
                }
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
    private void handleClearOrder() {
        // Show confirmation dialog
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Clear Order");
        confirmation.setHeaderText("Clear current order?");
        confirmation.setContentText("This will remove all items from your cart.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            clearCart();
        }
    }

    @FXML
    private void handleLogOut() throws IOException {

        try {
            new LoadFXML(
                    cafeShop,    // Your Cafe facade instance
                    primaryStage,     // pass existing stage
                    FxmlView.HELLO,   //access enum
                    800,            // Width
                    600            // Height
            ).load();
        } catch (IOException e) {
            // Handle error (show dialog, log, etc.)
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCheckout() {
        // Checks if the checkout button is clicked
        System.out.println("Checkout clicked!");

        // Validate name field
        String customerName = customerNameField.getText().trim();
        if (customerName.isEmpty()) {
            showAlert("Name Required", "Please fill in your name");
            return;
        }

        // Checks if the cart is empty
        if (cartItems.isEmpty()) {
            showAlert("Empty Cart", "Your cart is empty. Please add items before checkout.");
            return;
        }
        try {
            OrdersManagement ordersManagement = cafeShop.getOrdersManagement();
            String orderId = ordersManagement.createNewOrder(customerName);

            // Deduct ingredients from inventory
            if (!deductOrderIngredients()) {
                showAlert("Order Error",
                        "Some items are no longer available. Please review your order.");
                return;
            }

            for (int i = 0; i < cartItemObjects.size(); i++) {
                Object item = cartItemObjects.get(i);
                int quantity = cartItemQuantities.get(i);

                if (item instanceof BeverageItem beverage) {
                    BeverageSize size = cartItemSizes.get(i);
                    // Get customizations for this beverage (you'll need to track these)
                    List<CustomItem> customizations = getCustomizationsForItem(i);

                    OrderItem orderItem = ordersManagement.createBeverageItem(beverage, size,
                            customizations.isEmpty() ? null : customizations.get(0));

                    // Add remaining customizations
                    for (int c = 1; c < customizations.size(); c++) {
                        orderItem.modifyOrderItem(customizations.get(c));
                    }

                    for (int q = 0; q < quantity; q++) {
                        ordersManagement.addItemIntoOrder(orderId, orderItem);
                    }
                }
                else if (item instanceof PastriesItem) {
                    OrderItem orderItem = ordersManagement.createPastriesItem(
                            (PastriesItem)item);

                    for (int q = 0; q < quantity; q++) {
                        ordersManagement.addItemIntoOrder(orderId, orderItem);
                    }
                }
            }

            // 3. Finalize the order
            ordersManagement.finalizeActiveOrder(orderId);

            // 4. Clear cart
            clearCart();

            // 5. Show single success message
            showAlert("Success", "Order #" + orderId + " placed successfully!\nTotal: $" + String.format("%.2f", cartTotal));

        } catch (Exception e) {
            showAlert("Order Error", "An error occurred while placing your order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearCart() {
        cartItems.clear();
        cartItemObjects.clear();
        cartItemSizes.clear();
        cartItemQuantities.clear();
        cartItemCustomizations.clear();
        cartTotal = 0.0;

        // Reset the checkout button text
        checkoutButton.setText(String.format("Checkout ($%.2f)", cartTotal));

        // Clear any selection state
        currentBeverage = null;
        currentSize = null;
        selectedCustomizations.clear();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
