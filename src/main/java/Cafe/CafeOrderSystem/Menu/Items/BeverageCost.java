package Cafe.CafeOrderSystem.Menu.Items;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BeverageCost(
        double price,
        Map<Ingredients, Integer> ingredients
) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BeverageCost{");
        sb.append("price=").append(price);
        ingredients.forEach((k, v) -> {
            sb.append("[ ingredient= ").append(k);
            sb.append(", amount=").append(v).append("]");
        });
        sb.append("}");
        return sb.toString();
    }
}
