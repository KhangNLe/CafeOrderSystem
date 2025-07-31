package Cafe.CafeOrderSystem.UI;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cafe/CafeOrderSystem/hello-view.fxml"));
        Parent root = loader.load();
        
        // Pass stage to login controller
        HelloController helloController = loader.getController();
        helloController.setPrimaryStage(primaryStage);
        
    Scene scene = new Scene(root, 800, 600); // Initial size
    primaryStage.setScene(scene);
    primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Let JavaFX handle initialization
    }
}


