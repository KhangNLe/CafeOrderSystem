package Cafe.CafeOrderSystem.Inventory.Ingredients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Ingredient {
    private String ingredientName;
    private int quantity;

    /**
     * Simple Data Class
     * @param name name of ingredient
     * @param startingQuant initial quantity
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public Ingredient(
            @JsonProperty("ingredient")String name,
            @JsonProperty("amount")int startingQuant){
        this.ingredientName = name;
        this.quantity = startingQuant;
    }

    public String getName(){
        return this.ingredientName;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void setQuantity(int newQuant){
        this.quantity = newQuant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient other)) return false;
        return this.ingredientName.equals(other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientName);
    }


}
