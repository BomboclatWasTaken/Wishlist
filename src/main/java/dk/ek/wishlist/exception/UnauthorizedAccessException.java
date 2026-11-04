package dk.ek.wishlist.exception;

/// Brugeren prøver at ændre eller slette noget, der tilhører en anden
public class UnauthorizedAccessException extends WishlistException {

    public UnauthorizedAccessException() {
        super("You do not have permission to do this, this is not your wishlist");
    }
}
