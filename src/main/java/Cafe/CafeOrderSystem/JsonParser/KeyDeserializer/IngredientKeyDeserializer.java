package Cafe.CafeOrderSystem.JsonParser.KeyDeserializer;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class IngredientKeyDeserializer extends KeyDeserializer {
    @Override
    public Ingredients deserializeKey(String key, DeserializationContext context){
        return new Ingredients(key);
    }
}
