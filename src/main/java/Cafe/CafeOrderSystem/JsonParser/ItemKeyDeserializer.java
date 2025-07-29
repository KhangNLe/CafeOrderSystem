package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class ItemKeyDeserializer extends KeyDeserializer {
    @Override
    public Ingredients deserializeKey(String key, DeserializationContext context){
        return new Ingredients(key);
    }
}
