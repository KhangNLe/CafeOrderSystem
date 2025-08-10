package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.Menu.CafeMenu;
import Cafe.CafeOrderSystem.Menu.Items.BeverageItem;
import Cafe.CafeOrderSystem.Menu.Items.PastriesItem;
import Cafe.CafeOrderSystem.utility.FxmlView;
import Cafe.CafeOrderSystem.utility.LoadFXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// WORK ON IT
public class CustomerUiController {
    private Cafe cafeShop;
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
