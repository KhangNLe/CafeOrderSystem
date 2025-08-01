package Cafe.CafeOrderSystem.JsonParser.KeyDeserializer;

import Cafe.CafeOrderSystem.CatalogItems.BeverageType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class BeverageTypeDeserializer extends KeyDeserializer {
    @Override
    public BeverageType deserializeKey(String key, DeserializationContext context){
        return new BeverageType(key);
    }
}
