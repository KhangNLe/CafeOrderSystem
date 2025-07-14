module coffee.cafeordersystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;


    opens coffee.cafeordersystem to javafx.fxml;
    exports coffee.cafeordersystem.CatalogItems to com.fasterxml.jackson.databind;
    exports coffee.cafeordersystem.Menu.Items to com.fasterxml.jackson.databind;
    exports coffee.cafeordersystem.Ingredients to com.fasterxml.jackson.databind;
    exports coffee.cafeordersystem;
}