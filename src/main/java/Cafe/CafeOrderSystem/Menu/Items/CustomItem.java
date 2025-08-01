package Cafe.CafeOrderSystem.Menu.Items;

import Cafe.CafeOrderSystem.CatalogItems.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;
@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomItem (
        String id,
        String name,
        BeverageType type,
        double additionalPrice,
        Map<Ingredients, Integer> ingredients,
        Map<Ingredients, Ingredients> ingredientReplacement,
        Set<MenuType> applicableTo
){}