package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.CatalogItems.BeverageType;
import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import Cafe.CafeOrderSystem.Inventory.Ingredients.Ingredient;
import Cafe.CafeOrderSystem.JsonParser.KeyDeserializer.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class MapperFactory {
    public static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(Ingredient.class, new IngredientKeyDeserializer());
        module.addKeyDeserializer(MenuType.class, new MenuTypeDeserializer());
        module.addKeyDeserializer(BeverageType.class, new BeverageTypeDeserializer());
        module.addKeyDeserializer(BeverageSize.class, new BeverageSizeDeserializer());

        module.addKeySerializer(Ingredient.class, new ItemKeySerializer());
        module.addKeySerializer(MenuType.class, new ItemKeySerializer());
        module.addKeySerializer(BeverageType.class, new ItemKeySerializer());
        module.addKeySerializer(BeverageSize.class, new ItemKeySerializer());

        mapper.registerModule(module);
        return mapper;
    }
}
