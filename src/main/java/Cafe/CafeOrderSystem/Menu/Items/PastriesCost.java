package Cafe.CafeOrderSystem.Menu.Items;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PastriesCost (
        double price,
        Map<Ingredients, Integer> ingredients
){}
