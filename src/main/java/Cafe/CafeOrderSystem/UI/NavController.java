package Cafe.CafeOrderSystem.UI;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class NavController {

    private final StackPane rootPane;
    public final Stage primaryStage;
    
    public NavController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.rootPane = new StackPane();
        Scene scene = new Scene(rootPane, 800, 600);
        primaryStage.setScene(scene);
    }
    
    public void push(Parent node) {
        rootPane.getChildren().add(node);
    }
    
    public void pop() {
        if (rootPane.getChildren().size() > 1) {
            rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
        }
    }
    
    public void replace(Parent node) {
        rootPane.getChildren().clear();
        rootPane.getChildren().add(node);
    }
    
    public void show() {
        primaryStage.show();
    }
}

