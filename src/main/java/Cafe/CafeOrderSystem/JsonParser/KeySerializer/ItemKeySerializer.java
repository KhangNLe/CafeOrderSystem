package Cafe.CafeOrderSystem.JsonParser.KeySerializer;

import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.CatalogItems.BeverageType;
import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.CatalogItems.MenuType;
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

            if (value instanceof Ingredients ingredient) {
                gen.writeFieldName(ingredient.getJsonKey());
            } else if (value instanceof BeverageSize size) {
                gen.writeFieldName(size.getJsonKey()); // or custom
            } else if (value instanceof MenuType menuType) {
                gen.writeFieldName(menuType.getJsonKey());
            } else if (value instanceof BeverageType beverageType) {
                gen.writeFieldName(beverageType.getJsonKey());
            } else {
                throw new InvalidInputException("Unsupported key type: " + value.getClass());
            }
        } catch (IOException e){
            throw new BackendErrorException(
                    String.format("Could not serialize item key '%s'", value), e
            );
        }
    }
}