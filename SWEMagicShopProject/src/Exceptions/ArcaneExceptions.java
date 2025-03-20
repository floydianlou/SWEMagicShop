package Exceptions;

public class ArcaneExceptions {

    public static class ArcaneCreationException extends RuntimeException {
        public ArcaneCreationException(String message) {
            super(message);
        }
    }

    public static class ArcaneCountException extends RuntimeException {
        public ArcaneCountException(String message) {
            super(message);
        }
    }

    public static class ArcaneViewException extends RuntimeException {
        public ArcaneViewException(String message) {
            super(message);
        }
    }
}
