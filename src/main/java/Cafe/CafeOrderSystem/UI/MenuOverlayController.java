package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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

    public void setFacade(Cafe cafeShop) {
        this.cafeShop = cafeShop;
    }

    public void setMenuItemData(Object item, Stage stage, Runnable refreshCallback) {
        this.menuItem = item;
        this.stage = stage;
        this.refreshCallback = refreshCallback;
        
        if (item instanceof BeverageItem) {
            BeverageItem beverage = (BeverageItem) item;
            itemNameLabel.setText(beverage.name());
            setupBeverageUI(beverage);
            priceSpinner.setVisible(false); // Hide spinner for beverages
        } else if (item instanceof PastriesItem) {
            PastriesItem pastry = (PastriesItem) item;
            itemNameLabel.setText(pastry.name());
            setupPastryUI(pastry);
            priceSpinner.setVisible(true); // Show spinner for pastries
        }
        
        configureSpinner();
    }

    private void setupBeverageUI(BeverageItem beverage) {
        sizesContainer.getChildren().clear();
        beverage.cost().forEach((size, cost) -> {
            Label sizeLabel = new Label(
                String.format("%s: $%.2f", size.getSize(), cost.price())
            );
            sizesContainer.getChildren().add(sizeLabel);
        });
    }

    private void setupPastryUI(PastriesItem pastry) {
        sizesContainer.getChildren().clear();
        Label priceLabel = new Label(
            String.format("Current Price: $%.2f", pastry.cost().price())
        );
        sizesContainer.getChildren().add(priceLabel);
        priceSpinner.getValueFactory().setValue(pastry.cost().price());
    }

    private void configureSpinner() {
        SpinnerValueFactory.DoubleSpinnerValueFactory factory = 
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 100.0, 0.0, 0.01);
        priceSpinner.setValueFactory(factory);
    }

    @FXML
private void handleSave() {
    if (menuItem instanceof PastriesItem) {
        PastriesItem pastry = (PastriesItem) menuItem;
        double newPrice = priceSpinner.getValue();
        cafeShop.getCafeMenuManagement().modifyPastriesCost(
            getPastryIndex(pastry),
            newPrice
        );
        
    }
    // Add beverage modification logic here if needed
    
    if (refreshCallback != null) {
        System.out.println("call");
        refreshCallback.run();
    }
    if (stage != null) {
        stage.close();
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