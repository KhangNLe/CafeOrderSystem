module Cafe.CafeOrderSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires java.logging;


    opens Cafe.CafeOrderSystem to javafx.fxml;
    exports Cafe.CafeOrderSystem.CatalogItems to com.fasterxml.jackson.databind;
    exports Cafe.CafeOrderSystem.Menu.Items to com.fasterxml.jackson.databind;
    exports Cafe.CafeOrderSystem.Inventory.Ingredients to com.fasterxml.jackson.databind;
    exports Cafe.CafeOrderSystem.Orders to com.fasterxml.jackson.databind;
    exports Cafe.CafeOrderSystem.Roles to com.fasterxml.jackson.databind;
    exports Cafe.CafeOrderSystem.JsonParser to java.logging;
    exports Cafe.CafeOrderSystem;
    exports Cafe.CafeOrderSystem.JsonParser.CafeMenu to java.logging;
    exports Cafe.CafeOrderSystem.JsonParser.Authentication to java.logging;
    opens Cafe.CafeOrderSystem.Orders to com.fasterxml.jackson.databind;
    opens Cafe.CafeOrderSystem.Inventory.Ingredients to com.fasterxml.jackson.databind;
    opens Cafe.CafeOrderSystem.Roles to com.fasterxml.jackson.databind;
    opens Cafe.CafeOrderSystem.CatalogItems to com.fasterxml.jackson.databind;
}