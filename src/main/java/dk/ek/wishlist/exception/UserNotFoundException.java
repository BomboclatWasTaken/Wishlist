package dk.ek.wishlist.exception;

public class UserNotFoundException extends WishlistException {
    public UserNotFoundException(int id) {
        super("User with id " + id + " was not found");
    }
}
