package Cafe.CafeOrderSystem.UI;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Exceptions.InvalidModifyingException;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.Menu.Items.BeverageCost;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MenuOverlayController {
    private Cafe cafeShop;
    private Stage stage;
    private Runnable refreshCallback;
    private Object menuItem; // Could be BeverageItem or PastriesItem



    @FXML private Label itemNameLabel;
    @FXML private VBox sizesContainer;
    @FXML private Spinner<Double> priceSpinner;
    @FXML private VBox ingredientsContainer;
        @FXML private TabPane sizeTabs;
    @FXML private Tab priceTab;
    @FXML private GridPane pastryPriceGrid;
        
    @FXML private VBox ingredientOverlay;
    @FXML private Label sizeHeaderLabel;

    private final Map<BeverageSize, Spinner<Double>> sizePriceSpinners = new HashMap<>();
private final Map<Ingredients, Spinner<Integer>> pastryIngredientSpinners = new HashMap<>();
    private final Map<BeverageSize, Map<Ingredients, Spinner<Integer>>> ingredientSpinnersBySize = new HashMap<>();
    private Spinner<Double> pastryPriceSpinner;
    // private BeverageSize currentEditingSize;

    private static final BeverageSize SMALL = new BeverageSize("SMALL");
    private static final BeverageSize MEDIUM = new BeverageSize("MEDIUM");
    private static final BeverageSize LARGE = new BeverageSize("LARGE");


    public MenuOverlayController() {
    ingredientSpinnersBySize.put(SMALL, new HashMap<>());
    ingredientSpinnersBySize.put(MEDIUM, new HashMap<>());
    ingredientSpinnersBySize.put(LARGE, new HashMap<>());
}


    public void setFacade(Cafe cafeShop) {
        this.cafeShop = cafeShop;
    }

    public void setMenuItemData(Object item, Stage stage, Runnable refreshCallback) {
        this.menuItem = item;
        this.stage = stage;
        this.refreshCallback = refreshCallback;
        
        if (item instanceof BeverageItem) {
            BeverageItem beverage = (BeverageItem) item;
            setupBeverageUI(beverage);
        } else if (item instanceof PastriesItem) {
            PastriesItem pastry = (PastriesItem) item;
            setupPastryUI(pastry);
      }
    }

       private void setupBeverageUI(BeverageItem beverage) {
        itemNameLabel.setText(beverage.name());
        sizesContainer.getChildren().clear();
        sizePriceSpinners.clear();

        // Setup size tabs and price spinners
        beverage.cost().forEach((size, cost) -> {
            VBox sizeBox = new VBox(5);
            sizeBox.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5;");
            
            Label sizeLabel = new Label(size.getSize() + " Price:");
            sizeLabel.setStyle("-fx-font-weight: bold;");
            
            Spinner<Double> priceSpinner = new Spinner<>(0.01, 100.0, cost.price(), 0.01);
            priceSpinner.setEditable(true);
            sizePriceSpinners.put(size, priceSpinner);
            
            sizeBox.getChildren().addAll(sizeLabel, priceSpinner);
            sizesContainer.getChildren().add(sizeBox);
        });

        setupBeverageIngredients(beverage);
    }



    // @FXML
    // private void handleSaveIngredients() {
    //     if (menuItem instanceof BeverageItem && currentEditingSize != null) {
    //         BeverageItem beverage = (BeverageItem) menuItem;
    //         Map<Ingredients, Integer> newIngredients = new HashMap<>();
            
    //         ingredientSpinners.forEach((ingredient, spinner) -> {
    //             newIngredients.put(ingredient, spinner.getValue());
    //         });
            
    //         // Update only the current size's ingredients
    //         BeverageCost oldCost = beverage.cost().get(currentEditingSize);
    //         BeverageCost newCost = new BeverageCost(oldCost.price(), newIngredients);
    //         beverage.cost().put(currentEditingSize, newCost);
            
    //         closeIngredientOverlay();
    //     }
    // } NEW CHANGE
// private void setupBeverageIngredients(BeverageItem beverage) {
//     ingredientsContainer.getChildren().clear();
    
//     // Create grid
//     GridPane grid = new GridPane();
//     grid.setHgap(10);
//     grid.setVgap(5);
//     grid.setPadding(new Insets(10));

//     // Add header
//     grid.add(new Label("Ingredient"), 0, 0);
//     grid.add(new Label("Small"), 1, 0);
//     grid.add(new Label("Medium"), 2, 0);
//     grid.add(new Label("Large"), 3, 0);

//     // Add ingredients with spinners
//     int row = 1;
//     for (Ingredients ingredient : beverage.cost().values().iterator().next().ingredients().keySet()) {
//         grid.add(new Label(ingredient.getName()), 0, row);
        
//         // Small spinner
//         Spinner<Integer> smallSpinner = createSizeSpinner(beverage, SMALL, ingredient);
//         grid.add(smallSpinner, 1, row);
        
//         // Medium spinner
//         Spinner<Integer> mediumSpinner = createSizeSpinner(beverage, MEDIUM, ingredient);
//         grid.add(mediumSpinner, 2, row);
        
//         // Large spinner
//         Spinner<Integer> largeSpinner = createSizeSpinner(beverage, LARGE, ingredient);
//         grid.add(largeSpinner, 3, row);
        
//         row++;
//     }

//     ingredientsContainer.getChildren().add(grid);
// } NEW CHANGE

private void setupBeverageIngredients(BeverageItem beverage) {
    ingredientsContainer.getChildren().clear();

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(5);
    grid.setPadding(new Insets(10));

    grid.add(new Label("Ingredient"), 0, 0);
    grid.add(new Label("Small"), 1, 0);
    grid.add(new Label("Medium"), 2, 0);
    grid.add(new Label("Large"), 3, 0);

    // Clear any previous spinner refs
    ingredientSpinnersBySize.get(SMALL).clear();
    ingredientSpinnersBySize.get(MEDIUM).clear();
    ingredientSpinnersBySize.get(LARGE).clear();

    // Use the first size as the "ingredient universe"
    Set<Ingredients> ingredientSet = beverage.cost()
            .values()
            .iterator()
            .next()
            .ingredients()
            .keySet();

    int row = 1;
    for (Ingredients ingredient : ingredientSet) {
        grid.add(new Label(ingredient.getName()), 0, row);

        Spinner<Integer> smallSpinner  = createSizeSpinner(beverage, SMALL,  ingredient);
        Spinner<Integer> mediumSpinner = createSizeSpinner(beverage, MEDIUM, ingredient);
        Spinner<Integer> largeSpinner  = createSizeSpinner(beverage, LARGE,  ingredient);

        grid.add(smallSpinner,  1, row);
        grid.add(mediumSpinner, 2, row);
        grid.add(largeSpinner,  3, row);

        // Store for later read-back
        ingredientSpinnersBySize.get(SMALL).put(ingredient, smallSpinner);
        ingredientSpinnersBySize.get(MEDIUM).put(ingredient, mediumSpinner);
        ingredientSpinnersBySize.get(LARGE).put(ingredient, largeSpinner);

        row++;
    }

    ingredientsContainer.getChildren().add(grid);
}


private Spinner<Integer> createSizeSpinner(BeverageItem beverage, BeverageSize size, Ingredients ingredient) {
    int value = beverage.cost().get(size).ingredients().getOrDefault(ingredient, 0);
    Spinner<Integer> spinner = new Spinner<>(0, 1000, value);
    spinner.setEditable(true);
    spinner.setPrefWidth(80);
    // Optional: enforce numeric edits
    SpinnerValueFactory<Integer> vf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, value);
    spinner.setValueFactory(vf);
    return spinner;
}

            
    @FXML
    private void closeIngredientOverlay() {
        ingredientOverlay.setVisible(false);
    }


    private int getAverageQuantity(BeverageItem beverage, Ingredients ingredient) {
        return (int) beverage.cost().values().stream()
            .filter(cost -> cost.ingredients().containsKey(ingredient))
            .mapToInt(cost -> cost.ingredients().get(ingredient))
            .average()
            .orElse(0);
    }



private void setupPastryUI(PastriesItem pastry) {
    itemNameLabel.setText(pastry.name());
    sizesContainer.getChildren().clear();

    VBox sizeBox = new VBox(5);
    sizeBox.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5;");

    pastryPriceSpinner = new Spinner<>(0.01, 1000.0, pastry.cost().price(), 0.01);
    pastryPriceSpinner.setEditable(true);
    pastryPriceSpinner.setPrefWidth(120);

    sizeBox.getChildren().addAll(new Label("Price"), pastryPriceSpinner);
    sizesContainer.getChildren().add(sizeBox);

    setupPastryIngredients(pastry);
}

private void setupPastryIngredients(PastriesItem pastry) {
    ingredientsContainer.getChildren().clear();
    pastryIngredientSpinners.clear();

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(5);
    grid.setPadding(new Insets(10));

    // Headers
    grid.add(new Label("Ingredient"), 0, 0);
    grid.add(new Label("Amount"),     1, 0);

    int row = 1;
    // Use whatever is currently on the pastry as the “universe”
    for (Map.Entry<Ingredients, Integer> e : pastry.cost().ingredients().entrySet()) {
        Ingredients ingredient = e.getKey();
        int current = e.getValue();

        grid.add(new Label(ingredient.getName()), 0, row);

        Spinner<Integer> amount = new Spinner<>(0, 1000, current);
        amount.setEditable(true);
        amount.setPrefWidth(90);
        amount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, current));

        grid.add(amount, 1, row);
        pastryIngredientSpinners.put(ingredient, amount);

        row++;
    }

    ingredientsContainer.getChildren().add(grid);
}



@FXML
private void handleSave() {
    try {
if (menuItem instanceof PastriesItem pastry) {
    int pastryIndex = cafeShop.getCafeMenuManagement().getPastriesItems().indexOf(pastry);
    if (pastryIndex >= 0) {
        double newPrice = (pastryPriceSpinner != null)
                ? pastryPriceSpinner.getValue()
                : pastry.cost().price();

        // Collect updated ingredient amounts from the spinners
        Map<Ingredients, Integer> updatedIngredients = new HashMap<>();
        if (!pastryIngredientSpinners.isEmpty()) {
            pastryIngredientSpinners.forEach((ing, spin) -> updatedIngredients.put(ing, spin.getValue()));
        } else {
            // fallback (shouldn't happen if UI built)
            updatedIngredients.putAll(pastry.cost().ingredients());
        }

        // EITHER: combined method (recommended)
        cafeShop.getCafeMenuManagement().modifyPastryItem(
            pastryIndex, updatedIngredients, newPrice
        );

        // OR: if you’re keeping the split methods, do ingredients THEN price:
        // cafeShop.getCafeMenuManagement().modifyPastriesIngredientCost(updatedIngredients, pastryIndex);
        // cafeShop.getCafeMenuManagement().modifyPastriesCost(pastryIndex, newPrice);
    }

    if (refreshCallback != null) refreshCallback.run();
    if (stage != null) stage.close();
}

else if (menuItem instanceof BeverageItem beverage) {
    // Get the index once before looping
    int beverageIndex = cafeShop.getCafeMenuManagement()
        .getBeverageItems()
        .indexOf(beverage);

    if (beverageIndex >= 0) {
        for (BeverageSize size : new BeverageSize[]{SMALL, MEDIUM, LARGE}) {
            // Price: use spinner if present; otherwise keep current
            double newPrice = sizePriceSpinners.containsKey(size)
                ? sizePriceSpinners.get(size).getValue()
                : beverage.cost().get(size).price();

            // Ingredients for THIS size from stored spinners
            Map<Ingredients, Integer> updatedIngredients = new HashMap<>();
            Map<Ingredients, Spinner<Integer>> spinnersForSize = ingredientSpinnersBySize.get(size);
            if (spinnersForSize != null && !spinnersForSize.isEmpty()) {
                spinnersForSize.forEach((ingredient, spinner) ->
                    updatedIngredients.put(ingredient, spinner.getValue())
                );
            } else {
                // Fallback to existing
                updatedIngredients.putAll(beverage.cost().get(size).ingredients());
            }

            // Save changes for this size
            cafeShop.getCafeMenuManagement().modifyBeverageSize(
                beverageIndex, size, updatedIngredients, newPrice
            );
        }
    }

    if (refreshCallback != null) refreshCallback.run();
    if (stage != null) stage.close();
}

    // int beverageIndex = cafeShop.getCafeMenuManagement().getBeverageItems().indexOf(beverage);
    // if (beverageIndex >= 0) {
    //     for (Map.Entry<BeverageSize, Spinner<Double>> entry : sizePriceSpinners.entrySet()) {
    //         BeverageSize size = entry.getKey();
    //         double newPrice = entry.getValue().getValue();

    //         // Build updated ingredient map for THIS size
    //         Map<Ingredients, Integer> updatedIngredients = new HashMap<>();
    //         Map<Ingredients, Spinner<Integer>> spinnersForSize = ingredientSpinnersBySize.get(size);
    //         if (spinnersForSize != null) {
    //             spinnersForSize.forEach((ingredient, spinner) ->
    //                 updatedIngredients.put(ingredient, spinner.getValue())
    //             );
    //         }

    //         cafeShop.getCafeMenuManagement().modifyBeverageSize(
    //             beverageIndex, size, updatedIngredients, newPrice
    //         );
    //     }
    // }
        
        // Refresh and close
        if (refreshCallback != null) refreshCallback.run();
        if (stage != null) stage.close();
        
    } catch (Exception e) {
        e.printStackTrace();
        new Alert(Alert.AlertType.ERROR, "Failed to save changes: " + e.getMessage()).show();
    }
}


@FXML
private void handleCancel() {
    if (stage != null) {
        stage.close();
    }
}
    private int getPastryIndex(PastriesItem pastry) {
        return cafeShop.getCafeMenuManagement()
            .getPastriesItems()
            .indexOf(pastry);
    }
}