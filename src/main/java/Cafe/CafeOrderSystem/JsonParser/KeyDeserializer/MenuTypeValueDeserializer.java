package Cafe.CafeOrderSystem.JsonParser.KeyDeserializer;

import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MenuTypeValueDeserializer extends JsonDeserializer<MenuType> {
    @Override
    public MenuType deserialize(JsonParser p, DeserializationContext context) throws IOException{
        return  new MenuType(p.getValueAsString());
    }
}
