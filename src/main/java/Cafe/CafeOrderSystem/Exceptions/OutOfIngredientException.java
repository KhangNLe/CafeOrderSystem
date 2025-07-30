package Cafe.CafeOrderSystem.Exceptions;

public class OutOfIngredientException extends IllegalArgumentException {
    public OutOfIngredientException(String message) {
        super(message);
    }

    public OutOfIngredientException(String message, Throwable cause) {
        super(message, cause);
    }
}
