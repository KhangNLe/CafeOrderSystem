package coffee.cafeordersystem.Menu.Items;

import coffee.cafeordersystem.CatalogItems.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BeverageItem(
        String id,
        String name,
        Beverage type,
        Map<BeverageSize, BeverageCost> cost
        ){}