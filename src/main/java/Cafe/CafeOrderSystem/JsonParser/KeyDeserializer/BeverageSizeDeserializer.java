package Cafe.CafeOrderSystem.JsonParser.KeyDeserializer;

import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class BeverageSizeDeserializer extends KeyDeserializer {
    @Override
    public BeverageSize deserializeKey(String key, DeserializationContext context){
        return new BeverageSize(key);
    }
}
