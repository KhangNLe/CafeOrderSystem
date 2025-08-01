package Cafe.CafeOrderSystem.JsonParser.KeyDeserializer;

import Cafe.CafeOrderSystem.CatalogItems.BeverageType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BeverageTypeValueDeserializer extends JsonDeserializer<BeverageType> {
    @Override
    public BeverageType deserialize(JsonParser p, DeserializationContext context)throws IOException {
        return new BeverageType(p.getValueAsString());
    }
}
