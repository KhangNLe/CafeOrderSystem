package Cafe.CafeOrderSystem.Exceptions;

public class BackendErrorException extends IllegalArgumentException {
    public BackendErrorException(String message) {
        super(message);
    }

    public BackendErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
