package coffee.cafeordersystem.Ingredients;

import coffee.cafeordersystem.CatalogItems.*;

import java.util.Objects;
public record IngredientItem (Ingredients ingredient, double amount) {

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
