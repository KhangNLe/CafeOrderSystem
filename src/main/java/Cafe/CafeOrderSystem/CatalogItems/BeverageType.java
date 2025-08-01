package Cafe.CafeOrderSystem.CatalogItems;

import Cafe.CafeOrderSystem.JsonParser.JsonKey;

import java.util.Locale;

public class BeverageType implements JsonKey {
    private final String beverageType;

    public BeverageType(String beverageType){
        this.beverageType = beverageType.toUpperCase(Locale.ROOT);
    }

    @Override
    public String getJsonKey(){
        return this.beverageType;
    }

    public String getBeverageType(){
        return this.beverageType;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof BeverageType other)) return false;

        return this.beverageType.equals(other.getBeverageType());
    }

    @Override
    public int hashCode(){
        return this.beverageType.hashCode();
    }

    @Override
    public String toString(){
        return this.beverageType;
    }

}
