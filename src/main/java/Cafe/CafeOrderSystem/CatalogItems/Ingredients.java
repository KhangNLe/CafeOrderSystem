package Cafe.CafeOrderSystem.CatalogItems;

import Cafe.CafeOrderSystem.JsonParser.JsonKey;

import java.util.Locale;

public class Ingredients implements JsonKey {
    private final String name;

    public Ingredients(String name) {
        this.name = name.toUpperCase(Locale.ROOT);
    }

    @Override
    public String getJsonKey() {
        return this.name;
    }

    public String getName(){
        return this.name;
    }

}