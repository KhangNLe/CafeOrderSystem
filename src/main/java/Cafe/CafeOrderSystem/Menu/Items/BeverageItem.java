package Cafe.CafeOrderSystem.Menu.Items;

import Cafe.CafeOrderSystem.CatalogItems.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BeverageItem(
        String id,
        String name,
        Beverage type,
        Map<BeverageSize, BeverageCost> cost
        ){}