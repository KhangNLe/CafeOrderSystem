package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.utility.FxmlView;
import Cafe.CafeOrderSystem.utility.LoadFXML;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private Cafe cafeShop;
    private LoadFXML load;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    private final double LOGIN_WIDTH = 800;
    private final double LOGIN_HEIGHT = 600;
    
    private Stage primaryStage;

    ShowErrorDialog popup = new ShowErrorDialog();

    public void setFacade(Cafe cafeShop) {
        this.cafeShop = cafeShop;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Barista", "Manager");
        roleComboBox.setValue("Barista");
    }

    @FXML
    private void handleLogin() throws IOException {
        System.out.printf("%s %s", usernameField.getText(), passwordField.getText());
        try {
            if(usernameField.getText().equals("") || passwordField.getText().equals("")){
            throw new IllegalStateException();
        }
        } catch (Exception e) {
            popup.show("Error", "USERNAME / PASSWORD CANNOT BE NULL", e);
            return;
        }
        String role = roleComboBox.getValue();
        
        switch (role) {
            case "Barista" -> openBaristaScreen();
            case "Manager" -> openManagerScreen();
        }
    }

    private void openBaristaScreen() throws IOException {
        try {
        new LoadFXML(
            cafeShop,    // Your Cafe facade instance
            primaryStage,     // Or pass existing stage
            FxmlView.BARISTA,
            800,            // Width
            600             // Height
        ).load();
    } catch (IOException e) {
        // Handle error (show dialog, log, etc.)
        e.printStackTrace();
        }
    }



    private void openCustomerScreen() throws IOException {
    try {
        new LoadFXML(
            cafeShop,    // Your Cafe facade instance
            primaryStage,     // pass existing stage
            FxmlView.MANAGER,   //access enum
            800,            // Width
            600             // Height
        ).load();
    } catch (IOException e) {
        // Handle error (show dialog, log, etc.)
        e.printStackTrace();
        }
    }



    private void openManagerScreen() throws IOException {
    try {
        new LoadFXML(
            cafeShop,    // Your Cafe facade instance
            primaryStage,     // pass existing stage
            FxmlView.MANAGER,   //access enum
            800,            // Width
            600             // Height
        ).load();
    } catch (IOException e) {
        // Handle error (show dialog, log, etc.)
        e.printStackTrace();
        }
    }

}