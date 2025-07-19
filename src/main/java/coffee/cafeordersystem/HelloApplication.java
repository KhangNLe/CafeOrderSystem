package coffee.cafeordersystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load login screen first
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        
        // Pass stage to login controller
        LoginController loginController = loader.getController();
        loginController.setPrimaryStage(primaryStage);
        
    Scene scene = new Scene(root, 800, 600); // Initial size
    primaryStage.setScene(scene);
    primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Let JavaFX handle initialization
    }
}


