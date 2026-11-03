package dk.ek.wishlist.exception;

/// shareCode er ugyldig eller findes ikke
public class ShareCodeNotFoundException extends WishlistException  {
    public ShareCodeNotFoundException(String shareCode) {
        super("shareCode \"" + shareCode + "\" is unavailable or doesnt exist");
    }
}
