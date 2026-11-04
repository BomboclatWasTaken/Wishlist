package dk.ek.wishlist.exception;

public class WishNotFoundException extends WishlistException {
    public WishNotFoundException(int id) {
        super("Wish with id " + id + " was not found");
    }
}
