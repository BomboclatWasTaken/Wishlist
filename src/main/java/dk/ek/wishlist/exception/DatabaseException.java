package dk.ek.wishlist.exception;

/// Noget gik galt i databasen (pakker fx SQLException ind)
public class DatabaseException extends WishlistException {

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
