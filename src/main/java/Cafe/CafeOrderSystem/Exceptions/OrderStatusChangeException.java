package Cafe.CafeOrderSystem.Exceptions;

public class OrderStatusChangeException extends RuntimeException {
    public OrderStatusChangeException(String message) {
        super(message);
    }

    public OrderStatusChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderStatusChangeException(Throwable cause) {
        super("Failed to change the order status.", cause);
    }
}
