package dk.ek.wishlist.exceptions;

public class WishlistNotFoundException extends RuntimeException {
    public WishlistNotFoundException(int id) {
        super("Wishlist with id" + id + " was not found");
    }
}
