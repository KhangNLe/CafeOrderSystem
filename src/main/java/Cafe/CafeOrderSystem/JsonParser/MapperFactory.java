package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.Inventory.Ingredients.Ingredient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MapperFactory {
    public static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(Ingredient.class, new ItemKeyDeserializer());
        module.addKeySerializer(Ingredient.class, new ItemKeySerializer());
        mapper.registerModule(module);
        return mapper;
    }
}
