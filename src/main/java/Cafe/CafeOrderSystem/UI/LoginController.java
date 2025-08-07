package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.Exceptions.InvalidCredentialsException;
import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;
import Cafe.CafeOrderSystem.Roles.EmployeesAuthentication;
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
    String role = roleComboBox.getValue();
    String user = usernameField.getText();
    String pass = passwordField.getText();


    switch (role) {
        case "Barista" -> authBarista(user.trim(), pass); // don't trim passwords
        case "Manager" -> authManager(user.trim(), pass);
    }
}

private void authBarista(String user, String pass) {
    EmployeesAuthentication auth = cafeShop.getEmployeesAuthentication();
    try {
        if(user.equals("") || pass.equals("") ){
            throw new InvalidInputException("Username / Password must not be null");
        }
        boolean ok = auth.baristaLogin(user, pass);  // ✅ uses your existing backend
        if (ok) {
            openBaristaScreen();
        } else {
            throw new InvalidCredentialsException();
        }
    } catch (Exception e) {
       popup.show("Error", e.getMessage(), e);
    }
}

private void authManager(String user, String pass) {
    EmployeesAuthentication auth = cafeShop.getEmployeesAuthentication();
    try {
        if(user.equals("") || pass.equals("") ){
            throw new InvalidInputException("Username / Password must not be null");
        }
        boolean ok = auth.managerLogin(user, pass);  // ✅ uses your existing backend
        if (ok) {
            openManagerScreen();
        } else {
            throw new InvalidCredentialsException();
        }
    } catch (Exception e) {
       popup.show("Error", e.getMessage(), e);
    }
}
        

    private void openBaristaScreen() throws IOException {
 
        new LoadFXML(
            cafeShop,    // Your Cafe facade instance
            primaryStage,     // Or pass existing stage
            FxmlView.BARISTA,
            800,            // Width
            600             // Height
        ).load();
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