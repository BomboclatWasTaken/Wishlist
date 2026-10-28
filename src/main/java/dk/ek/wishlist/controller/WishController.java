package dk.ek.wishlist.controller;

import dk.ek.wishlist.service.WishService;
import dk.ek.wishlist.service.WishlistService;

public class WishController {

    /// Initialisere WishService klassen som final
    private final WishService wishService;

    /// Initialisere WishlistService klassen som final
    private final WishlistService wishlistService;

    /// Konstruktør
    public WishController(WishService wishService, WishlistService wishlistService){
        this.wishService = wishService;
        this.wishlistService = wishlistService;
    }
}
