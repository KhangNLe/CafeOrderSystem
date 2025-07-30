package Cafe.CafeOrderSystem.Exceptions;

public class OutOfIngredientException extends CafeSystemException {
    public OutOfIngredientException(String message) {
        super(message);
    }

    public OutOfIngredientException(String message, Throwable cause) {
        super(message, cause);
    }
}
