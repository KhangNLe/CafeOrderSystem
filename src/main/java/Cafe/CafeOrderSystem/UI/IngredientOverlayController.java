package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.Inventory.Inventory;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class IngredientOverlayController {
    private IngredientItem item;
    private Stage stage;
    private Runnable refreshCallback;
    private Cafe cafeShop;

    @FXML 
    private Label ingredientIdLabel;
    
    @FXML 
    private Label currentUnitsLabel;
    
    @FXML 
    private Spinner<Integer> newUnitsSpinner;



    public void setFacade(Cafe cafeShop) {
        this.cafeShop = cafeShop;
    }

    public void setIngredientData(IngredientItem item, Stage stage, Runnable refreshCallback) {
        this.item = item;
        this.stage = stage;
        this.refreshCallback = refreshCallback;
        
        ingredientIdLabel.setText("Ingredient: " + item.getIngredient().getName());
        updateCurrentUnitsDisplay();
        configureSpinner();
    }

    private void updateCurrentUnitsDisplay() {
        if (cafeShop != null && item != null) {
            int currentUnits = cafeShop.getInventoryManagement().getList().getIngredients().getOrDefault(item, 0);
            currentUnitsLabel.setText(String.valueOf(currentUnits));
            newUnitsSpinner.getValueFactory().setValue(currentUnits);
        }
    }

    private void configureSpinner() {
        SpinnerValueFactory.IntegerSpinnerValueFactory factory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        newUnitsSpinner.setValueFactory(factory);
    }


    //look over later

    @FXML
private void handleSave() {
    if (item != null && cafeShop != null) {
        int newValue = newUnitsSpinner.getValue();
        Inventory inventory = cafeShop.getInventoryManagement();
        
        // Get current quantity
        int currentValue = inventory.getList().getIngredients().getOrDefault(item, 0);

        boolean success = inventory.modifyInventory(
            newValue,
            item  // Use the existing IngredientItem instead of creating new one
        );

        System.out.println(success);

        if (success && refreshCallback != null) {
            refreshCallback.run();
        }

        stage.close();
    }
}
    @FXML
    private void handleRemove() {
        int ingredientIndex = cafeShop.getInventoryManagement().getList().findObject(item);

        if (cafeShop.getIngredientList().findObject(item) != -1 && refreshCallback != null) {
                cafeShop.getIngredientList().removeObject(ingredientIndex);
                refreshCallback.run();
        }
        
        stage.close();
    }


    @FXML
    private void handleCancel() {
        if (stage != null) {
            stage.close();
        }
    }
}