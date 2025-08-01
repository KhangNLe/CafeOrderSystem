package Cafe.CafeOrderSystem.JsonParser.KeySerializer;

import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MenuTypeValueSerializer extends JsonSerializer<MenuType> {
    @Override
    public void serialize(MenuType value, JsonGenerator gen,
                          SerializerProvider p) throws IOException{
        gen.writeString(value.getJsonKey());
    }
}
