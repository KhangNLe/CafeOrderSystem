package Cafe.CafeOrderSystem.JsonParser.KeySerializer;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IngredientValueSerializer extends JsonSerializer<Ingredients> {
    @Override
    public void serialize(Ingredients value, JsonGenerator gen,
                          SerializerProvider s) throws IOException{
        gen.writeString(value.getName());
    }
}
