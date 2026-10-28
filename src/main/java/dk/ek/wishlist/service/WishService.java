package dk.ek.wishlist.service;

import dk.ek.wishlist.repository.WishRepository;
import dk.ek.wishlist.repository.WishlistRepository;

public class WishService {

    /// Initialisere WishlistRepository som final
    private final WishlistRepository wishlistRepository;

    /// Initialisere WishRepository som final
    private final WishRepository wishRepository;

    /// Konstruktør
    public WishService(WishlistRepository wishlistRepository, WishRepository wishRepository){
        this.wishlistRepository = wishlistRepository;
        this.wishRepository = wishRepository;
    }
}
