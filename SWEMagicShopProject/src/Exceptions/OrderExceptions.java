package Exceptions;

// This class gathers all specific exceptions for order making, so that the GUI (apparently) will manage them
// better knowing exactly what happened - instead of throwing a normal Exception everytime

public class OrderExceptions {

    public static class EmptyCartException extends Exception {
        public EmptyCartException(String message) {
            super(message);
        }
    }

    public static class MissingFundsException extends Exception {
        public MissingFundsException(String message) {
            super(message);
        }
    }

    public static class OrderSaveException extends Exception {
        public OrderSaveException(String message) {
            super(message);
        }
    }

    public static class ItemNotInCartException extends Exception {
        public ItemNotInCartException(String message) {
            super(message);
        }
    }
}


