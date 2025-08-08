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
    String role = roleComboBox.getValue().trim();
    String user = usernameField.getText().trim();
    String pass = passwordField.getText();


    if (validateRole(role, user, pass)) {
        switch (role) {
            case "Barista" -> openBaristaScreen(); // don't trim passwords
            case "Manager" -> openManagerScreen();
        }
    }
}


private boolean validateRole(String role, String userName, String password) {
        try {
            if (role.isEmpty() || userName.isEmpty() || password.isEmpty()) {
                throw new InvalidInputException("Role or Username or password must not be empty");
            }

            EmployeesAuthentication auth = cafeShop.getEmployeesAuthentication();
            if (!auth.validateCredentials(role, userName, password)) {
                throw new InvalidInputException(
                        String.format("Role %s username or password is incorrect", role)
                );
            }

            return true;
        } catch (InvalidInputException e){
            popup.show("Error", e.getMessage(), e);
        }

        return false;
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