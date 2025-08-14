package Cafe.CafeOrderSystem.UI;

import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.JsonParser.OrderItem.CustomerOrderParser;
import Cafe.CafeOrderSystem.Orders.CafeOrders;
import Cafe.CafeOrderSystem.Orders.OrdersManagement;
import Cafe.CafeOrderSystem.utility.FxmlView;
import Cafe.CafeOrderSystem.utility.LoadFXML;
import javafx.application.Application;
import javafx.fxml.FXML;
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
        // Initialize cafe first
        this.cafeShop = new Cafe();
        cafeShop.startShop();
        this.primaryStage = primaryStage;


        try {
            new LoadFXML(
                cafeShop,
                primaryStage,
                FxmlView.HELLO,
                800,
                600
            ).load();
        } catch (IOException e) {
            e.printStackTrace();
            throw e; // Re-throw to fail fast during development
        }
    }

        public void setFacade(Cafe cafeShop){
            this.cafeShop = cafeShop;
        }

        public void setPrimaryStage(Stage stage) {
            this.primaryStage = stage;
        }


    private void openLoginScreen() throws IOException {
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
    
       
    

    @Override
    public void stop(){
        cafeShop.closeShop();
    }

    public static void main(String[] args) {
        launch(args); // Let JavaFX handle initialization
    }
}


