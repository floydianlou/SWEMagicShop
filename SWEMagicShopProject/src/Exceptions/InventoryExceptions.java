package Exceptions;

public class InventoryExceptions {
    public static class InventoryUpdateException extends Exception {
        public InventoryUpdateException(String message) {
            super(message);
        }
    }
}
