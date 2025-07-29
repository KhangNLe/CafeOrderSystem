package Cafe.CafeOrderSystem.CatalogItems;

import Cafe.CafeOrderSystem.JsonParser.JsonKey;

public class BeverageSize implements JsonKey {
    private final String size;

    public BeverageSize(String size){
        this.size = size;
    }

    @Override
    public String getJsonKey(){
        return this.size;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if (!(o instanceof BeverageSize other)) return false;

        return this.size.equals(other.getSize());
    }

    public String getSize(){
        return this.size;
    }
}