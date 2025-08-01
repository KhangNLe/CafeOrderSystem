package Cafe.CafeOrderSystem.JsonParser.KeyDeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;

import java.io.IOException;

public class BeverageSizeValueDeserializer extends JsonDeserializer<BeverageSize> {
    @Override
    public BeverageSize deserialize(JsonParser p, DeserializationContext context) throws IOException{
        return new BeverageSize(p.getValueAsString());
    }
}
