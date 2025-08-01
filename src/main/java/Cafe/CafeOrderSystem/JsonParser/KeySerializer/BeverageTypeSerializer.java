package Cafe.CafeOrderSystem.JsonParser.KeySerializer;

import Cafe.CafeOrderSystem.CatalogItems.BeverageType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BeverageTypeSerializer extends JsonSerializer<BeverageType> {
    @Override
    public void serialize(BeverageType value, JsonGenerator gen,
                          SerializerProvider p) throws IOException{
        gen.writeString(value.getJsonKey());
    }
}
