package Cafe.CafeOrderSystem.Inventory.Ingredients;

import Cafe.CafeOrderSystem.CatalogItems.Ingredients;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientItem{
    private final String id;
    private final Ingredients ingredient;
    private int amount;

    @JsonCreator
    public IngredientItem(
            @JsonProperty("id") String id,
            @JsonProperty("ingredient") Ingredients ingredient,
            @JsonProperty("amount") int amount
    ){
        this.id = id;
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public String getId(){
        return id;
    }

    public Ingredients getIngredient(){
        return ingredient;
    }

    public int getAmount(){
        return amount;
    }

    public void changeAmount(int amount){
        this.amount += amount;
    }
    @Override
    public String toString() {
        return "Ingredients: " + ingredient +
                ", Amount: " + amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientItem other)) return false;
        return ingredient.equals(other.ingredient) && amount == other.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, amount);
    }
}
