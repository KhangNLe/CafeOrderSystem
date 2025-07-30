package Cafe.CafeOrderSystem.Exceptions;

public class InvalidInputException extends CafeSystemException{
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
