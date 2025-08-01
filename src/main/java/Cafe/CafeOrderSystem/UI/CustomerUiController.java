package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;


public class CustomerUiController {
    private Cafe cafeShop;
   // @FXML private ListView<MenuItemData> menuListView;
    @FXML private Button checkoutButton;
    @FXML private Button logoutButton;

    private Stage primaryStage;

    public void setFacade(Cafe  cafeShop) {
        this.cafeShop = cafeShop;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void handleLogOut() throws IOException {
        // Load hello screen first
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cafe/CafeOrderSystem/hello-view.fxml"));
        Parent root = loader.load();

        // Get the stage from the button
        Stage stage = (Stage) logoutButton.getScene().getWindow();

        // Get the controller and pass the stage forward
        HelloController helloController = loader.getController();
        helloController.setPrimaryStage(stage);

        // Set the scene
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Welcome");
    }

    @FXML
    private void handleCheckout() {
        System.out.println("Checkout clicked!");
    }

    /*
    @FXML
    public void initialize() {
        menuListView.setCellFactory(listView -> new MenuItemCellController());

        menuListView.setItems(FXCollections.observableArrayList(
                new MenuItemData("Latte", "/images/latte-small.jpg"),
                new MenuItemData("Cappuccino", "/images/cappuccinos.jpg"),
                new MenuItemData("Croissant", "/images/croissants.jpg")
        ));
    }

     */
}
