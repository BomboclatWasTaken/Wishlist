package dk.ek.wishlist.exception;

/// Forkert email eller kodeord ved login
public class InvalidLoginException extends WishlistException {

    public InvalidLoginException() {
        super("Wrong email or password");
    }
}
