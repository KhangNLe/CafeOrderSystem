package Cafe.CafeOrderSystem.Exceptions;

public class InvalidModifyingException extends IllegalArgumentException {
    public InvalidModifyingException(String message) {
        super(message);
    }

    public InvalidModifyingException(String message, Throwable cause) {
        super(message, cause);
    }
}
