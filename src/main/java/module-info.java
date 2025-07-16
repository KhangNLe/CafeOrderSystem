module Cafe.CafeOrderSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires java.logging;


    opens Cafe.CafeOrderSystem to javafx.fxml;
    exports Cafe.CafeOrderSystem.CatalogItems to com.fasterxml.jackson.databind;
    exports Cafe.CafeOrderSystem.Menu.Items to com.fasterxml.jackson.databind;
    exports Cafe.CafeOrderSystem.Ingredients to com.fasterxml.jackson.databind;
    exports Cafe.CafeOrderSystem.JsonParser to java.logging;
    exports Cafe.CafeOrderSystem;
}