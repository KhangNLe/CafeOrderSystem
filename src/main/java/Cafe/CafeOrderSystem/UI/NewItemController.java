package Cafe.CafeOrderSystem.UI;

import java.util.UUID;
import java.util.function.Consumer;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NewItemController {

    // Wiring/context
    private Stage stage;
    private Runnable refreshCallback;
    private IngredientList ingredientList;              // inject this via LoadFXML.configureController
    private Consumer<IngredientItem> ingredientSaver;   // optional external saver

    // FXML controls (must exist in your FXML)
    @FXML private TextField ingredientNameField;
    @FXML private Spinner<Integer> startingStockSpinner;
    @FXML private Label ingredientPreviewLabel;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;

    @FXML
    private void initialize() {
        // Spinner config
        startingStockSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1_000_000, 0, 1)
        );
        startingStockSpinner.setEditable(true);

        // Live preview
        ingredientNameField.textProperty().addListener((obs, o, n) -> updatePreview());
        startingStockSpinner.valueProperty().addListener((obs, o, n) -> updatePreview());

        updatePreview();
    }

    // --- Public setters for wiring ---
    public void setStage(Stage stage) { this.stage = stage; }
    public void setRefreshCallback(Runnable r) { this.refreshCallback = r; }
    public void setIngredientList(IngredientList list) { this.ingredientList = list; }
    public void setIngredientSaver(Consumer<IngredientItem> saver) { this.ingredientSaver = saver; }

    // --- Actions ---
@FXML
private void handleSave() {
    try {
        String name = trim(ingredientNameField.getText());
        if (name.isEmpty()) throw new IllegalArgumentException("Please enter an ingredient name.");

        int addAmount = safeValue(startingStockSpinner, 0);
        if (addAmount < 0) throw new IllegalArgumentException("Starting stock must be ≥ 0.");

        // ← NEW: duplicate check (case-insensitive by ingredient display name)
        if (ingredientList != null && findByIngredientNameIgnoreCase(name) >= 0) {
            throw new IllegalArgumentException("Ingredient \"" + name + "\" already exists.");
        }

        IngredientItem newItem = new IngredientItem(
            UUID.randomUUID().toString(),
            new Ingredients(name),
            addAmount
        );

        if (ingredientSaver != null) {
            ingredientSaver.accept(newItem);
        } else if (ingredientList != null) {
            // safe add (saveIntoIngredientList will also guard)
            saveIntoIngredientList(newItem);
        } else {
            throw new IllegalStateException("No IngredientList or saver provided.");
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
    // --- UI helpers ---
    private void updatePreview() {
        String name = trim(ingredientNameField.getText());
        int stock = safeValue(startingStockSpinner, 0);
        String text = name.isEmpty() ? "—" : name + " — Stock: " + stock;
        ingredientPreviewLabel.setText(text);
    }

    private static String trim(String s) { return s == null ? "" : s.trim(); }
    private static <T> T safeValue(Spinner<T> s, T fallback) {
        try { T v = s.getValue(); return v == null ? fallback : v; }
        catch (Exception e) { return fallback; }
    }
}
