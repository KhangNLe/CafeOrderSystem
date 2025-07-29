package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ItemKeySerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializerProvider){
        try {
            if (!(value instanceof Ingredients ingredient)) {
                throw new IllegalArgumentException("Ingredient must be of type Ingredients");
            }
            gen.writeFieldName(ingredient.getJsonKey());
        } catch (IOException e){
            throw new IllegalStateException(e);
        }
    }
}
