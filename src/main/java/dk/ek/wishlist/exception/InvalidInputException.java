package dk.ek.wishlist.exception;

/// Ugyldigt input, fx tomt navn, negativ pris eller ugyldig email
public class InvalidInputException extends WishlistException {

    public InvalidInputException(String message) {
        super(message);
    }
}