package Cafe.CafeOrderSystem.Menu.Items;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PastriesCost (
        double price,
        Map<Ingredients, Integer> ingredients
){
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PastriesCost{");
        sb.append("price=").append(price);
        ingredients.forEach((k, v) -> {
           sb.append("[ingredient= ").append(k);
           sb.append(", amount= ").append(v).append("]");
        });
        sb.append("}");
        return sb.toString();
    }

    public PastriesCost copyOf() {
        Map<Ingredients, Integer> newIngredients = ingredients.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new PastriesCost(price, newIngredients);
    }
}
