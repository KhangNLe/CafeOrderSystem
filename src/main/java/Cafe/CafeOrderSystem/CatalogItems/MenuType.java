package Cafe.CafeOrderSystem.CatalogItems;

import Cafe.CafeOrderSystem.JsonParser.JsonKey;

public class MenuType implements JsonKey {
    private final String type;

    public MenuType(String type){
        this.type = type;
    }

    @Override
    public String getJsonKey(){
        return this.type;
    }

    public String getMenuType(){
        return this.type;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof MenuType other)) return false;

        return type.equals(other.getMenuType());
    }

    @Override
    public int hashCode(){
        return type.hashCode();
    }
}
