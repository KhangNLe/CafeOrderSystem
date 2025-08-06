package Cafe.CafeOrderSystem.UI;
import Cafe.CafeOrderSystem.Cafe;
import Cafe.CafeOrderSystem.Exceptions.InvalidModifyingException;
import Cafe.CafeOrderSystem.Exceptions.OrderStatusChangeException;
import Cafe.CafeOrderSystem.Orders.CustomerOrder;
import Cafe.CafeOrderSystem.Orders.OrderStatus;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import Cafe.CafeOrderSystem.Cafe;
import javafx.stage.Stage;

public class Controller {
    private Cafe cafeShop;
    private Stage primaryStage;
    
    public void setFacade(Cafe cafeShop){
        this.cafeShop = cafeShop;
    }
    
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
