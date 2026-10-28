package dk.ek.wishlist.controller;

import dk.ek.wishlist.service.WishService;
import dk.ek.wishlist.service.WishlistService;

public class SharedListController {

    /// Initialisere WishService klassen som final
    private final WishService wishService;

    /// Initialisere WishlistService klassen som final
    private final WishlistService wishlistService;

    /// Konstruktør
    public SharedListController(WishlistService wishlistService, WishService wishService){
        this.wishlistService = wishlistService;
        this.wishService = wishService;
    }
}
