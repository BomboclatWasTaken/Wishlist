package dk.ek.wishlist.exception;

/// Email er allerede i brug ved oprettelse af bruger
public class EmailAlreadyExistsException extends WishlistException {

    public EmailAlreadyExistsException(String email) {
        super("This email " + email + " is already used");
    }
}
