package Cafe.CafeOrderSystem.JsonParser.KeyDeserializer;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class IngredientValueDeserializer extends JsonDeserializer<Ingredients> {
    @Override
    public Ingredients deserialize(JsonParser p, DeserializationContext context) throws IOException{
        return new Ingredients(p.getValueAsString());
    }
}
