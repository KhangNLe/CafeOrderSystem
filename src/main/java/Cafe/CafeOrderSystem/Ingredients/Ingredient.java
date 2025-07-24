package Cafe.CafeOrderSystem.Ingredients;

public class Ingredient {
    private String ingredientName;
    private int quantity;

    public Ingredient(String name, int startingQuant){
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
}
