package Cafe.CafeOrderSystem.UI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientList;
import Cafe.CafeOrderSystem.Menu.Items.PastriesCost;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NewItemController {

    private Cafe cafeShop;

    //Tabs
    @FXML private TabPane typeTabPane;
    @FXML private Tab ingredientTab;
    @FXML private Tab beverageTab;
    @FXML private Tab pastryTab;

    // Wiring/context
    private Stage stage;
    private Runnable refreshCallback;
    private IngredientList ingredientList; // inject this via LoadFXML.configureController
    private Consumer<Object> itemSaver;  // optional external saver

    // FXML controls (must exist in your FXML)
    //Ingredients
    @FXML private TextField ingredientNameField;
    @FXML private Spinner<Integer> startingStockSpinner;
    @FXML private Label ingredientPreviewLabel;

    //Pastries
    @FXML private TextField pastryNameField;
    @FXML private Spinner<Double> pastryPriceSpinner;


    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;



    // Add these new fields to NewItemController
    @FXML private ListView<IngredientItem> availableIngredientsListView;
    @FXML private ListView<IngredientItem> selectedIngredientsListView;
    @FXML private Spinner<Integer> ingredientQuantitySpinner;
    @FXML private Button addIngredientButton;
    @FXML private Button removeIngredientButton;

    private Map<IngredientItem, Integer> selectedIngredients = new HashMap<>();



    @FXML
    private void initialize() {
        // Ingredients Spinner config
        startingStockSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1_000_000, 0, 1)
        );
        startingStockSpinner.setEditable(true);

        // Initialize pastry controls
        pastryPriceSpinner.setValueFactory(
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 100.0, 2.99, 0.5)
        );
        pastryPriceSpinner.setEditable(true);
            // Ingredients list setup
        ingredientQuantitySpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1)
        );

        
    
    // Button actions
    addIngredientButton.setOnAction(e -> addSelectedIngredient());
    removeIngredientButton.setOnAction(e -> removeSelectedIngredient());


        // Live preview
        ingredientNameField.textProperty().addListener((obs, o, n) -> updatePreview());
        startingStockSpinner.valueProperty().addListener((obs, o, n) -> updatePreview());

        // Set up live preview updates (come back to check)
        // ingredientNameField.textProperty().addListener((obs, o, n) -> updatePreview());
        // startingStockSpinner.valueProperty().addListener((obs, o, n) -> updatePreview());
        // pastryNameField.textProperty().addListener((obs, o, n) -> updatePreview());
        // pastryPriceSpinner.valueProperty().addListener((obs, o, n) -> updatePreview());

        updatePreview();

         availableIngredientsListView.setCellFactory(lv -> new ListCell<IngredientItem>() {
        @Override
        protected void updateItem(IngredientItem item, boolean empty) {
            super.updateItem(item, empty);
            setText(item == null ? "" : item.getName());
        }
    });

    selectedIngredientsListView.setCellFactory(lv -> new ListCell<IngredientItem>() {
        @Override
        protected void updateItem(IngredientItem item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText("");
            } else {
                int quantity = selectedIngredients.get(item);
                setText(item.getName() + " (" + quantity + ")");
            }
        }
    });
    }

    


    // --- Public setters for wiring ---
    public void setStage(Stage stage) { this.stage = stage; }
    public void setFacade(Cafe cafeShop){this.cafeShop = cafeShop;}
    public void setRefreshCallback(Runnable r) { this.refreshCallback = r; }
    public void setIngredientList(IngredientList list) { this.ingredientList = list; }
    public void setItemSaver(Consumer<Object> saver) { this.itemSaver = saver; }

    // --- Actions ---
private void addSelectedIngredient() {
    IngredientItem selected = availableIngredientsListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
        int quantity = ingredientQuantitySpinner.getValue();
        selectedIngredients.put(selected, quantity);
        refreshSelectedListView();
    }
}

private void removeSelectedIngredient() {
    IngredientItem selected = selectedIngredientsListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
        selectedIngredients.remove(selected);
        refreshSelectedListView();
    }
}

private void refreshSelectedListView() {
    selectedIngredientsListView.getItems().clear();
    selectedIngredients.forEach((ingredient, quantity) -> {
        selectedIngredientsListView.getItems().add(ingredient);
    });
}

private Map<Ingredients, Integer> getSelectedIngredients() {
    Map<Ingredients, Integer> ingredients = new HashMap<>();
    selectedIngredients.forEach((item, quantity) -> {
        ingredients.put(item.getIngredient(), quantity);
    });
    return ingredients;
}

@FXML
private void handleSave() {
    try {
        Tab selectedTab = typeTabPane.getSelectionModel().getSelectedItem();
        
        if (selectedTab.equals(ingredientTab)) {
            saveIngredient();
        } else if (selectedTab.equals(pastryTab)) {
            savePastry();
        } else {
            throw new IllegalArgumentException("Unsupported item type");
        }

        if (refreshCallback != null) refreshCallback.run();
        if (stage != null) stage.close();

    } catch (Exception e) {
        new Alert(Alert.AlertType.ERROR, "Failed to save: " + e.getMessage()).show();
        e.printStackTrace();
    }
}

    private void saveIngredient() {
        String name = trim(ingredientNameField.getText());
        if (name.isEmpty()) throw new IllegalArgumentException("Please enter an ingredient name.");

        int addAmount = safeValue(startingStockSpinner, 0);
        if (addAmount < 0) throw new IllegalArgumentException("Starting stock must be ≥ 0.");

        if (ingredientList != null && findByIngredientNameIgnoreCase(name) >= 0) {
            throw new IllegalArgumentException("Ingredient \"" + name + "\" already exists.");
        }

        IngredientItem newItem = new IngredientItem(
            UUID.randomUUID().toString(),
            new Ingredients(name),
            addAmount
        );

        if (itemSaver != null) {
            itemSaver.accept(newItem);
        } else if (ingredientList != null) {
            try {
                saveIntoIngredientList(newItem);
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save Ingredient: " + e.getMessage()).show();
                e.printStackTrace();
            }
            
        } else {
            throw new IllegalStateException("No IngredientList or saver provided.");
        }
    }



        private void savePastry() {
    try {
        String name = trim(pastryNameField.getText());
        if (name.isEmpty()) throw new IllegalArgumentException("Please enter a pastry name.");

        double price = safeValue(pastryPriceSpinner, 0.0);
        if (price <= 0) throw new IllegalArgumentException("Price must be greater than 0.");

        // Get selected ingredients with quantities
        Map<Ingredients, Integer> ingredients = getSelectedIngredients();

        PastriesItem newPastry = new PastriesItem(
            UUID.randomUUID().toString(),
            name,
            new MenuType("PASTRY"),
            new PastriesCost(price, ingredients)
        );

        if (itemSaver != null) {
            itemSaver.accept(newPastry);
        } else if (cafeShop != null) {
            cafeShop.getCafeMenuManagement().getPastriesItems().add(newPastry);
        } else {
            throw new IllegalStateException("No way to save pastry item");
        }
    } catch (Exception e) {
        new Alert(Alert.AlertType.ERROR, "Failed to save pastry: " + e.getMessage()).show();
        throw e;
    }
    }

        private void updatePreview() {
        Tab selectedTab = typeTabPane.getSelectionModel().getSelectedItem();
        
        if (selectedTab == ingredientTab) {
            String name = trim(ingredientNameField.getText());
            int stock = safeValue(startingStockSpinner, 0);
            String text = name.isEmpty() ? "—" : name + " — Stock: " + stock;
            ingredientPreviewLabel.setText(text);
        } else if (selectedTab == pastryTab) {
            String name = trim(pastryNameField.getText());
            double price = safeValue(pastryPriceSpinner, 0.0);


            String text = name.isEmpty() ? "—" : 
                String.format("%s (%s) - $%.2f", name, price);
            ingredientPreviewLabel.setText(text); // Reusing same label for simplicity
        }
    }


    @FXML
    private void handleCancel() {
        if (stage != null) stage.close();
    }

private void saveIntoIngredientList(IngredientItem toAdd) throws Exception {
    // Reject if same ingredient already exists
    if (findByIngredientNameIgnoreCase(toAdd.getIngredient().getName()) >= 0) {
        throw new IllegalArgumentException(
            "Ingredient \"" + toAdd.getIngredient().getName() + "\" already exists."
        );
    }
    ingredientList.addObject(toAdd);
    // if your JsonCollection needs an explicit write, call it here
}



private int findByIngredientNameIgnoreCase(String name) {
    if (name == null) return -1;
    String needle = name.trim();
    for (int i = 0; ; i++) {
        try {
            IngredientItem it = ingredientList.getObject(i);
            String got = it.getIngredient().getName();
            if (got != null && got.equalsIgnoreCase(needle)) return i;
        } catch (Exception e) {
            return -1; // reached end
        }
    }
}
    // // --- UI helpers ---
    // private void updatePreview() {
    //     String name = trim(ingredientNameField.getText());
    //     int stock = safeValue(startingStockSpinner, 0);
    //     String text = name.isEmpty() ? "—" : name + " — Stock: " + stock;
    //     ingredientPreviewLabel.setText(text);
    // }

    public ListView<IngredientItem> getAvailableIngredientsListView() {
    return availableIngredientsListView;
}

public ListView<IngredientItem> getSelectedIngredientsListView() {
    return selectedIngredientsListView;
}


    private static String trim(String s) { return s == null ? "" : s.trim(); }
    private static <T> T safeValue(Spinner<T> s, T fallback) {
        try { T v = s.getValue(); return v == null ? fallback : v; }
        catch (Exception e) { return fallback; }
    }
}
