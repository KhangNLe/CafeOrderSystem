package Cafe.CafeOrderSystem.Orders;

public enum OrderStatus {
    PENDING, IN_PROCESS, READY;

    public boolean changeStatusAttempt(OrderStatus newStatus) {
        switch (this){
            case PENDING ->{
                if (newStatus != PENDING) {
                    return true;
                }
            }
            case IN_PROCESS ->{
                if (newStatus == READY){
                    return true;
                }
            }
        }
        return false;
    }
}
