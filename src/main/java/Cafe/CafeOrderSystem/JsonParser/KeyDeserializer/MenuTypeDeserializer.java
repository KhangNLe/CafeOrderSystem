package Cafe.CafeOrderSystem.JsonParser.KeyDeserializer;

import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class MenuTypeDeserializer extends KeyDeserializer {
    @Override
    public MenuType deserializeKey(String key, DeserializationContext context){
        return new  MenuType(key);
    }
}
