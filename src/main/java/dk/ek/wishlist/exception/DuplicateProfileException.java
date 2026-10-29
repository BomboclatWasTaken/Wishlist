package dk.ek.wishlist.exception;

public class DuplicateProfileException extends RuntimeException {
    public DuplicateProfileException(String message) {
        super(message);
    }
}
