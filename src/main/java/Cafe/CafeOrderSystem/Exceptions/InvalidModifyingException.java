package Cafe.CafeOrderSystem.Exceptions;

public class InvalidModifyingException extends CafeSystemException {
    public InvalidModifyingException(String message) {
        super(message);
    }

    public InvalidModifyingException(String message, Throwable cause) {
        super(message, cause);
    }
}
