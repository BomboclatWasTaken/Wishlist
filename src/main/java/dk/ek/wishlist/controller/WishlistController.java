package dk.ek.wishlist.controller;

import dk.ek.wishlist.service.WishlistService;

public class WishlistController {

    /// Initialisere WishlistService klassen som final
    private final WishlistService wishlistService;

    /// Konstruktør
    public WishlistController(WishlistService wishlistService){
        this.wishlistService = wishlistService;
    }

}
