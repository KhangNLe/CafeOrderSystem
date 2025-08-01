package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private Cafe cafeShop;
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
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cafe/CafeOrderSystem/barista-view.fxml"));
    Parent root = loader.load();
    
    // Get the controller JavaFX created
    BaristaUiController baristaController = loader.getController();


    baristaController.setFacade(cafeShop);
    // Pass the stage forward
    baristaController.setPrimaryStage(primaryStage);
    
    // Show the new screen
    primaryStage.setScene(new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT));
    primaryStage.setTitle("Barista Dashboard");
    }



    private void openCustomerScreen() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cafe/CafeOrderSystem/customer-view.fxml"));
        Parent root = loader.load();

        // Get the controller JavaFX created
        CustomerUiController customerController = loader.getController();

        customerController.setFacade(cafeShop);
        // Pass the stage forward
        customerController.setPrimaryStage(primaryStage);

        // Show the new screen
        primaryStage.setScene(new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT));
        primaryStage.setTitle("Customer Dashboard");

    }

    private void openManagerScreen() throws IOException {

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cafe/CafeOrderSystem/manager-view.fxml"));
    Parent root = loader.load();
    
    // Get the controller JavaFX created
    ManagerController managerController = loader.getController();

    managerController.setFacade(cafeShop);
    // Pass the stage forward
    managerController.setPrimaryStage(primaryStage);
    
    // Show the new screen
    primaryStage.setScene(new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT));
    primaryStage.setTitle("Manager Dashboard");
    }

    private void showScene(Parent root, String title) {
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}