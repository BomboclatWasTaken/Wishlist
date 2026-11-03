package dk.ek.wishlist.exceptions;

public class WishNotFoundException extends RuntimeException {
    public WishNotFoundException(int id) {
        super("Wish with id" + id + " was not found");
    }
}
