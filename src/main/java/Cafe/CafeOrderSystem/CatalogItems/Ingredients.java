package Cafe.CafeOrderSystem.CatalogItems;

import Cafe.CafeOrderSystem.JsonParser.JsonKey;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Locale;

public class Ingredients implements JsonKey {
    private final String name;

    @JsonCreator
    public Ingredients(@JsonProperty("ingredient") String name) {
        this.name = name.toUpperCase(Locale.ROOT);
    }

    @Override
    public String getJsonKey() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredients other)) return false;

        return this.name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}