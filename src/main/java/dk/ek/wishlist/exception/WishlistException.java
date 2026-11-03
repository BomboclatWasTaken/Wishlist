package dk.ek.wishlist.exception;

public class WishlistException extends RuntimeException {
    public WishlistException(String message) {
        super(message);
    }

    public WishlistException(String message, Throwable cause) {
        super(message, cause);
    }
}
