package Cafe.CafeOrderSystem.JsonParser;

import Cafe.CafeOrderSystem.CatalogItems.BeverageSize;
import Cafe.CafeOrderSystem.CatalogItems.BeverageType;
import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import Cafe.CafeOrderSystem.CatalogItems.MenuType;
import Cafe.CafeOrderSystem.Inventory.Ingredients.Ingredient;
import Cafe.CafeOrderSystem.Inventory.Ingredients.IngredientItem;
import Cafe.CafeOrderSystem.JsonParser.KeyDeserializer.*;
import Cafe.CafeOrderSystem.JsonParser.KeySerializer.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Factory class for creating a customized {@link ObjectMapper} with support for
 * application-specific serialization and deserialization logic.
 *
 * <p>
 * This mapper supports:
 * <ul>
 *     <li>Key deserialization for complex map keys such as {@link IngredientItem}, {@link MenuType}
 *     , {@link BeverageType}, and {@link BeverageSize}</li>
 *     <li>Value deserialization for domain models like {@link Ingredients}, {@link MenuType}, and {@link BeverageSize}</li>
 *     <li>Custom serializers and key serializers for those same types</li>
 * </ul>
 * </p>
 *
 * <p>Use this mapper when reading or writing JSON involving domain models that have custom parsing rules.</p>
 *
 * <p>This factory is stateless and safe for concurrent use.</p>
 */
public class MapperFactory {

    /**
     * Creates and returns a configured {@link ObjectMapper} instance with custom serializers
     * and deserializers
     *
     * @return a customized ObjectMapper
     */
    public static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(Ingredients.class, new IngredientKeyDeserializer());
        module.addKeyDeserializer(MenuType.class, new MenuTypeDeserializer());
        module.addKeyDeserializer(BeverageType.class, new BeverageTypeDeserializer());
        module.addKeyDeserializer(BeverageSize.class, new BeverageSizeDeserializer());

        module.addDeserializer(Ingredients.class, new IngredientValueDeserializer());
        module.addDeserializer(BeverageType.class, new BeverageTypeValueDeserializer());
        module.addDeserializer(MenuType.class, new MenuTypeValueDeserializer());
        module.addDeserializer(BeverageSize.class, new BeverageSizeValueDeserializer());

        module.addKeySerializer(IngredientItem.class, new ItemKeySerializer());
        module.addKeySerializer(MenuType.class, new ItemKeySerializer());
        module.addKeySerializer(BeverageType.class, new ItemKeySerializer());
        module.addKeySerializer(BeverageSize.class, new ItemKeySerializer());

        module.addSerializer(Ingredients.class, new IngredientValueSerializer());
        module.addSerializer(BeverageType.class, new BeverageTypeSerializer());
        module.addSerializer(BeverageSize.class, new BeverageSizeSerializer());
        module.addSerializer(MenuType.class, new MenuTypeValueSerializer());

        mapper.registerModule(module);
        return mapper;
    }
}
