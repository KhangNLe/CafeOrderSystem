package coffee.cafeordersystem.Menu.Items;

import coffee.cafeordersystem.CatalogItems.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;
@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomItem (
        String id,
        String name,
        CustomBeverage type,
        double additionalPrice,
        Map<Ingredients, Integer> ingredients,
        Map<Ingredients, Ingredients> ingredientReplacement,
        List<Beverage> applicableTo
){}

