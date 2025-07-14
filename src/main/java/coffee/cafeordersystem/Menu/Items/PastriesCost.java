package coffee.cafeordersystem.Menu.Items;

import coffee.cafeordersystem.CatalogItems.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PastriesCost (
        double price,
        Map<Ingredients, Integer> ingredients
){}
