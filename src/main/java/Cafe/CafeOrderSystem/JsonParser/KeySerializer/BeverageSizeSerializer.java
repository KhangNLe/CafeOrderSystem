package Cafe.CafeOrderSystem.JsonParser.KeySerializer;

import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.CatalogItems.BeverageType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BeverageSizeSerializer extends JsonSerializer<BeverageSize> {
    @Override
    public void serialize(BeverageSize beverageSize, JsonGenerator gen,
                          SerializerProvider serializerProvider) throws IOException {
        gen.writeString(beverageSize.getJsonKey());
    }
}
