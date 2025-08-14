package Cafe.CafeOrderSystem.UI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientList;
import Cafe.CafeOrderSystem.Menu.Items.BeverageCost;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesCost;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NewItemController {

    private Cafe cafeShop;

    // Tabs
    @FXML private TabPane typeTabPane;
    @FXML private Tab ingredientTab;
    @FXML private Tab beverageTab;
    @FXML private Tab pastryTab;

    // Wiring/context
    private Stage stage;
    private Runnable refreshCallback;
    private IngredientList ingredientList; // inject this via LoadFXML.configureController
    private Consumer<Object> itemSaver;    // optional external saver

    // Ingredient tab
    @FXML private TextField ingredientNameField;
    @FXML private Spinner<Integer> startingStockSpinner;
    @FXML private Label ingredientPreviewLabel;

    // Pastry tab
    @FXML private TextField pastryNameField;
    @FXML private Spinner<Double> pastryPriceSpinner;
    @FXML private ListView<IngredientItem> availableIngredientsListView;
    @FXML private ListView<IngredientItem> selectedIngredientsListView;
    @FXML private Spinner<Integer> ingredientQuantitySpinner;
    @FXML private Button addIngredientButton;    // optional: only needed if not using onAction in FXML
    @FXML private Button removeIngredientButton; // optional: only needed if not using onAction in FXML

    // Beverage tab
    @FXML private TextField beverageNameField;
    @FXML private ListView<IngredientItem> beverageAvailableIngredientsListView;
    @FXML private ListView<IngredientItem> beverageIngredientsListView;
    @FXML private ListView<String> beverageSizeListView;
    @FXML private Spinner<Double> smallPriceSpinner;
    @FXML private Spinner<Double> mediumPriceSpinner;
    @FXML private Spinner<Double> largePriceSpinner;
    @FXML private Spinner<Integer> beverageQuantitySpinner;


    // Per-size ingredient maps
    private final Map<BeverageSize, Map<IngredientItem,Integer>> sizeIngredientMaps = new HashMap<>();
    private static final BeverageSize SMALL  = new BeverageSize("SMALL");
    private static final BeverageSize MEDIUM = new BeverageSize("MEDIUM");
    private static final BeverageSize LARGE  = new BeverageSize("LARGE");
    private BeverageSize currentSize = SMALL;

    // Pastry selected ingredients (single map)
    private final Map<IngredientItem,Integer> selectedIngredients = new HashMap<>();

    @FXML
    private void initialize() {
        // Init per-size maps BEFORE any cell factories/listeners use them
        sizeIngredientMaps.put(SMALL,  new HashMap<>());
        sizeIngredientMaps.put(MEDIUM, new HashMap<>());
        sizeIngredientMaps.put(LARGE,  new HashMap<>());

        // Ingredient tab
        if (startingStockSpinner != null) {
            startingStockSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1_000_000, 0, 1)
            );
            startingStockSpinner.setEditable(true);
        }

        // Pastry tab
        if (pastryPriceSpinner != null) {
            pastryPriceSpinner.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 100.0, 2.99, 0.5)
            );
            pastryPriceSpinner.setEditable(true);
        }
        if (ingredientQuantitySpinner != null) {
            ingredientQuantitySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1)
            );
        }

        

        // Beverage tab
        if (beverageQuantitySpinner != null) {
    beverageQuantitySpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1)
    );
}
        beverageTab.setDisable(false);
        smallPriceSpinner.setValueFactory(
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 100.0, 2.99, 0.5));
        mediumPriceSpinner.setValueFactory(
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 100.0, 3.99, 0.5));
        largePriceSpinner.setValueFactory(
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 100.0, 4.99, 0.5));

        // Populate size choices and keep currentSize in sync (fixes VerifyError)
        beverageSizeListView.getItems().setAll("Small", "Medium", "Large");
        beverageSizeListView.getSelectionModel().selectFirst();
        currentSize = SMALL;
        beverageSizeListView.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            if (nv == null) return;
            switch (nv.toUpperCase()) {
                case "SMALL"  -> currentSize = SMALL;
                case "MEDIUM" -> currentSize = MEDIUM;
                case "LARGE"  -> currentSize = LARGE;
                default       -> currentSize = SMALL;
            }
            refreshBeverageIngredientsView();
        });

        // Cell factories (single definitions, null-safe)
        if (availableIngredientsListView != null) {
            availableIngredientsListView.setCellFactory(lv -> new ListCell<>() {
                @Override protected void updateItem(IngredientItem item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getName());
                }
            });
        }
       if (beverageAvailableIngredientsListView != null) {
        beverageAvailableIngredientsListView.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(IngredientItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
    }
    if (beverageIngredientsListView != null) {
    beverageIngredientsListView.setCellFactory(lv -> new ListCell<>() {
        @Override protected void updateItem(IngredientItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) { setText(""); return; }
            int qty = sizeIngredientMaps.getOrDefault(currentSize, Map.of())
                                         .getOrDefault(item, 0);
            setText(item.getName() + " (" + qty + ")");
        }
    });
}
        if (selectedIngredientsListView != null) {
            selectedIngredientsListView.setCellFactory(lv -> new ListCell<>() {
                @Override protected void updateItem(IngredientItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) { setText(""); return; }
                    int qty = selectedIngredients.getOrDefault(item, 0);
                    setText(item.getName() + " (" + qty + ")");
                }
            });
        }

        // Wire pastry buttons IF you chose fx:id approach (safe if absent)
        if (addIngredientButton != null)    addIngredientButton.setOnAction(e -> addSelectedIngredient());
        if (removeIngredientButton != null) removeIngredientButton.setOnAction(e -> removeSelectedIngredient());

        // Live preview
        if (ingredientNameField != null)
            ingredientNameField.textProperty().addListener((o, ov, nv) -> updatePreview());
        if (startingStockSpinner != null)
            startingStockSpinner.valueProperty().addListener((o, ov, nv) -> updatePreview());
        if (pastryNameField != null)
            pastryNameField.textProperty().addListener((o, ov, nv) -> updatePreview());
        if (pastryPriceSpinner != null)
            pastryPriceSpinner.valueProperty().addListener((o, ov, nv) -> updatePreview());

        updatePreview();
    }

    // --- Public setters for wiring ---
    public void setStage(Stage stage) { this.stage = stage; }
    public void setFacade(Cafe cafeShop){ this.cafeShop = cafeShop; }
    public void setRefreshCallback(Runnable r) { this.refreshCallback = r; }
    public void setIngredientList(IngredientList list) { 
        this.ingredientList = list; 
        // Optionally pre-populate the Available lists when injected
        tryPopulateAvailableLists();
    }
    public void setItemSaver(Consumer<Object> saver) { this.itemSaver = saver; }

    // --- Pastry actions ---
    @FXML
    private void addSelectedIngredient() {
        IngredientItem selected = availableIngredientsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            int quantity = ingredientQuantitySpinner.getValue();
            selectedIngredients.put(selected, quantity);
            refreshSelectedListView();
        }
    }
    @FXML
    private void removeSelectedIngredient() {
        IngredientItem selected = selectedIngredientsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedIngredients.remove(selected);
            refreshSelectedListView();
        }
    }
    private void refreshSelectedListView() {
        selectedIngredientsListView.getItems().setAll(selectedIngredients.keySet());
    }
    private Map<Ingredients,Integer> getSelectedIngredients() {
        Map<Ingredients,Integer> out = new HashMap<>();
        selectedIngredients.forEach((item, qty) -> out.put(item.getIngredient(), qty));
        return out;
    }

    // --- Beverage actions ---
@FXML
private void addBeverageIngredient() {
    IngredientItem selected = beverageAvailableIngredientsListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
        int quantity = (beverageQuantitySpinner != null ? beverageQuantitySpinner.getValue()
                                                        : ingredientQuantitySpinner.getValue());
        sizeIngredientMaps.get(currentSize).put(selected, quantity);
        refreshBeverageIngredientsView();
    }
}
    @FXML
    private void removeBeverageIngredient() {
        IngredientItem selected = beverageIngredientsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            sizeIngredientMaps.getOrDefault(currentSize, new HashMap<>()).remove(selected);
            refreshBeverageIngredientsView();
        }
    }
    private void refreshBeverageIngredientsView() {
        Map<IngredientItem,Integer> m = sizeIngredientMaps.getOrDefault(currentSize, Map.of());
        beverageIngredientsListView.getItems().setAll(m.keySet());
    }

    // --- Save handlers ---
    @FXML
    private void handleSave() {
        try {
            Tab selectedTab = typeTabPane.getSelectionModel().getSelectedItem();
            if (selectedTab.equals(ingredientTab)) {
                saveIngredient();
            } else if (selectedTab.equals(pastryTab)) {
                savePastry();
            } else if (selectedTab.equals(beverageTab)) {
                saveBeverage();
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
    @FXML
    private void handleCancel() {
        if (stage != null) stage.close();
    }

    private void saveIngredient() {
        String name = trim(ingredientNameField.getText());
        if (name.isEmpty()) throw new IllegalArgumentException("Please enter an ingredient name.");
        int addAmount = safeValue(startingStockSpinner, 0);
        if (addAmount < 0) throw new IllegalArgumentException("Starting stock must be ≥ 0.");
        if (ingredientList != null && findByIngredientNameIgnoreCase(name) >= 0) {
            throw new IllegalArgumentException("Ingredient \"" + name + "\" already exists.");
        }
        IngredientItem newItem = new IngredientItem(UUID.randomUUID().toString(), new Ingredients(name), addAmount);
        if (itemSaver != null) {
            itemSaver.accept(newItem);
        } else if (ingredientList != null) {
            try { saveIntoIngredientList(newItem); }
            catch (Exception e) {
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

            Map<Ingredients,Integer> ingredients = getSelectedIngredients();
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

    private void saveBeverage() {
        System.out.println("[saveBeverage] itemSaver=" + (itemSaver != null) + " cafeShop=" + (cafeShop != null));
        try {
            String name = trim(beverageNameField.getText());
            if (name.isEmpty()) throw new IllegalArgumentException("Please enter a beverage name.");
            Map<BeverageSize,BeverageCost> costs = getBeverageCosts();
            BeverageItem newBeverage = new BeverageItem(
                UUID.randomUUID().toString(),
                name,
                new MenuType("BEVERAGE"),
                costs
            );
            if (itemSaver != null) {
                itemSaver.accept(newBeverage);
            } else if (cafeShop != null) {
                cafeShop.getCafeMenuManagement().getBeverageItems().add(newBeverage);
            } else {
                throw new IllegalStateException("No way to save beverage item");
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save beverage: " + e.getMessage()).show();
            throw e;
        }
    }

    private Map<BeverageSize,BeverageCost> getBeverageCosts() {
        Map<BeverageSize,BeverageCost> costs = new HashMap<>();
        costs.put(SMALL,  new BeverageCost(safeValue(smallPriceSpinner,  0.0), convertToIngredientsMap(sizeIngredientMaps.get(SMALL))));
        costs.put(MEDIUM, new BeverageCost(safeValue(mediumPriceSpinner, 0.0), convertToIngredientsMap(sizeIngredientMaps.get(MEDIUM))));
        costs.put(LARGE,  new BeverageCost(safeValue(largePriceSpinner,  0.0), convertToIngredientsMap(sizeIngredientMaps.get(LARGE))));
        return costs;
    }
    private Map<Ingredients,Integer> convertToIngredientsMap(Map<IngredientItem,Integer> input) {
        Map<Ingredients,Integer> out = new HashMap<>();
        if (input != null) input.forEach((item, qty) -> out.put(item.getIngredient(), qty));
        return out;
    }

    private void updatePreview() {
        Tab selectedTab = typeTabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == ingredientTab) {
            String name = trim(ingredientNameField.getText());
            int stock = safeValue(startingStockSpinner, 0);
            ingredientPreviewLabel.setText(name.isEmpty() ? "—" : name + " — Stock: " + stock);
        } else if (selectedTab == pastryTab) {
            String name = trim(pastryNameField.getText());
            double price = safeValue(pastryPriceSpinner, 0.0);
            ingredientPreviewLabel.setText(name.isEmpty() ? "—" : String.format("%s - $%.2f", name, price));
        } else {
            ingredientPreviewLabel.setText("—");
        }
    }

    // --- Utility/wiring helpers ---
    public ListView<IngredientItem> getAvailableIngredientsListView() { return availableIngredientsListView; }
    public ListView<IngredientItem> getSelectedIngredientsListView()  { return selectedIngredientsListView; }
    public void setItemSaverAndRefresh(Consumer<Object> saver, Runnable refresh) { this.itemSaver = saver; this.refreshCallback = refresh; }

    private void saveIntoIngredientList(IngredientItem toAdd) throws Exception {
        if (findByIngredientNameIgnoreCase(toAdd.getIngredient().getName()) >= 0)
            throw new IllegalArgumentException("Ingredient \"" + toAdd.getIngredient().getName() + "\" already exists.");
        ingredientList.addObject(toAdd);
    }
    private int findByIngredientNameIgnoreCase(String name) {
        if (ingredientList == null || name == null) return -1;
        String needle = name.trim();
        for (int i = 0; ; i++) {
            try {
                IngredientItem it = ingredientList.getObject(i);
                String got = it.getIngredient().getName();
                if (got != null && got.equalsIgnoreCase(needle)) return i;
            } catch (Exception e) { return -1; }
        }
    }

    private void tryPopulateAvailableLists() {
        if (ingredientList == null) return;
        try {
            // Pull all items from IngredientList (using same pattern as findByIngredientNameIgnoreCase)
            var all = new java.util.ArrayList<IngredientItem>();
            for (int i = 0; ; i++) {
                try {
                    all.add(ingredientList.getObject(i));
                } catch (Exception end) { break; }
            }
            if (availableIngredientsListView != null)
                availableIngredientsListView.getItems().setAll(all);
            if (beverageAvailableIngredientsListView != null)
                beverageAvailableIngredientsListView.getItems().setAll(all);
        } catch (Exception ignored) {}
    }

    private static String trim(String s) { return s == null ? "" : s.trim(); }
    private static <T> T safeValue(Spinner<T> s, T fallback) {
        try { T v = s.getValue(); return v == null ? fallback : v; }
        catch (Exception e) { return fallback; }
    }
}
