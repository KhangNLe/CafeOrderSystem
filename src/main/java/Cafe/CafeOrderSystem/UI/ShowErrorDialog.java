package Cafe.CafeOrderSystem.UI;

import javafx.scene.control.Alert;

public class ShowErrorDialog {

public void show(String title, String message, Exception e){
Alert alert = new Alert(Alert.AlertType.ERROR);
alert.setTitle("Error");
alert.setHeaderText(message);
alert.setContentText(e.getMessage());
alert.showAndWait();
}
}
