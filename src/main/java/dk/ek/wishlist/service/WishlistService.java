package dk.ek.wishlist.service;

import dk.ek.wishlist.repository.WishlistRepository;

public class WishlistService {

    /// Initialisere WishlistRepository klassen som final
    private final WishlistRepository wishlistRepository;

    /// Konstruktør
    public WishlistService(WishlistRepository wishlistRepository){
        this.wishlistRepository = wishlistRepository;
    }

}
