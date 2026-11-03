package dk.ek.wishlist.exception;

public class WishlistNotFoundException extends WishlistException {
    public WishlistNotFoundException(int id) {
        super("Wishlist with id " + id + " was not found");
    }
}
