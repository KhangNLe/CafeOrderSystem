package Cafe.CafeOrderSystem.Exceptions;

public abstract class CafeSystemException extends  IllegalArgumentException {
    public CafeSystemException(String message) {
        super(message);
    }

    public CafeSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
