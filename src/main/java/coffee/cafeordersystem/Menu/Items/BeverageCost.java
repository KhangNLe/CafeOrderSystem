package coffee.cafeordersystem.Menu.Items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import coffee.cafeordersystem.CatalogItems.*;

import java.util.*;
@JsonIgnoreProperties(ignoreUnknown = true)
public record BeverageCost(
        double price,
        Map<Ingredients, Integer> ingredients
) { }
