package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.utility.FxmlView;
import Cafe.CafeOrderSystem.utility.LoadFXML;
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
    private Cafe cafeShop;
    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        cafeShop = new Cafe();
        cafeShop.startShop();
        this.primaryStage = primaryStage;
        // Load login screen first
    //     FXMLLoader loader = new FXMLLoader(getClass().getResource("/Cafe/CafeOrderSystem/hello-view.fxml"));
    //     Parent root = loader.load();

    //     // Pass stage to login controller
    //     HelloController helloController = loader.getController();
    //     helloController.setFacade(cafeShop);
    //     helloController.setPrimaryStage(primaryStage);

    // Scene scene = new Scene(root, 800, 600); // Initial size
    // primaryStage.setScene(scene);
    // primaryStage.show();
    try {
        new LoadFXML(
            cafeShop,    // Your Cafe facade instance
            primaryStage,     // pass existing stage
            FxmlView.LOGIN,   //access enum
            800,            // Width
            600             // Height
        ).load();
    } catch (IOException e) {
        // Handle error (show dialog, log, etc.)
        e.printStackTrace();
        }

    openLoginScreen();
    }

    private void openLoginScreen() throws IOException {
    
    }

    @Override
    public void stop(){
        cafeShop.closeShop();
    }

    public static void main(String[] args) {
        launch(args); // Let JavaFX handle initialization
    }
}


