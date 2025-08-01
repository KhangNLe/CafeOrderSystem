package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Exceptions.BackendErrorException;
import Cafe.CafeOrderSystem.Exceptions.InvalidInputException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ItemKeySerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializerProvider){
        try {
            if (!(value instanceof Ingredients ingredient)) {
                throw new InvalidInputException("Ingredient must be of type Ingredients");
            }
            gen.writeString(ingredient.getJsonKey());
        } catch (IOException e){
            throw new BackendErrorException(
                    String.format("Could not serialize item key '%s'", value), e
            );
        }
    }
}
