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

            IngredientItem newItem = new IngredientItem(
                UUID.randomUUID().toString(),
                new Ingredients(name),   // your Ingredients is a class, not enum
                addAmount
            );

            if (ingredientSaver != null) {
                ingredientSaver.accept(newItem);
            } else if (ingredientList != null) {
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

    // --- Persistence helpers ---
    private void saveIntoIngredientList(IngredientItem toAdd) throws Exception {
        int idx = findByIngredient(toAdd.getIngredient());
        if (idx >= 0) {
            IngredientItem existing = ingredientList.getObject(idx);
            int combined = Math.max(0, existing.getAmount() + toAdd.getAmount());
            existing.changeAmount(combined);
        } else {
            ingredientList.addObject(toAdd);
        }
        // If your JsonCollection needs an explicit write/flush, call it here.
    }

    /** Linear scan because JsonCollection doesn’t expose a map by ingredient. */
    private int findByIngredient(Ingredients ingredient) {
        for (int i = 0; ; i++) {
            try {
                IngredientItem it = ingredientList.getObject(i);
                if (it.getIngredient().equals(ingredient)) return i;
            } catch (Exception e) {
                return -1;
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
