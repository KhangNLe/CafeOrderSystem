module coffee.cafeordersystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens coffee.cafeordersystem to javafx.fxml;
    exports coffee.cafeordersystem;
}