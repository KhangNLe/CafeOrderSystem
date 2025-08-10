package Cafe.CafeOrderSystem.JsonParser.KeySerializer;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.Exceptions.BackendErrorException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IngredientKeySerializer extends JsonSerializer<Ingredients> {

    @Override
    public void serialize(Ingredients ingredients, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (ingredients == null){
            throw new BackendErrorException(
                    String.format("Ingredient %s is not a Ingredients object", ingredients)
            );
        }
        jsonGenerator.writeFieldName(ingredients.getJsonKey());
    }
}
