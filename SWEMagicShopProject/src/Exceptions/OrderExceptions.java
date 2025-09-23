package Exceptions;

public class OrderExceptions {

    public static class EmptyCartException extends RuntimeException {
        public EmptyCartException(String message) {
            super(message);
        }
    }

    public static class MissingFundsException extends RuntimeException {
        public MissingFundsException(String message) {
            super(message);
        }
    }

    public static class OrderSaveException extends RuntimeException {
        public OrderSaveException(String message) {
            super(message);
        }
    }

    public static class ItemNotInCartException extends RuntimeException {
        public ItemNotInCartException(String message) {
            super(message);
        }
    }
}