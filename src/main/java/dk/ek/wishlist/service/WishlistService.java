package dk.ek.wishlist.service;

import dk.ek.wishlist.model.Wishlist;
import dk.ek.wishlist.repository.WishlistRepository;

public class WishlistService {

    /// Initialisere WishlistRepository klassen som final
    private final WishlistRepository wishlistRepository;

    /// Konstruktør
    public WishlistService(WishlistRepository wishlistRepository){
        this.wishlistRepository = wishlistRepository;
    }

    /// Henter en specifik liste, hvis den tilhører brugeren
    public Wishlist getSpecificList(int listId, int userId) {
        Wishlist wishlist = wishlistRepository.findById(listId);

        //if statement tjekker om wishlist er null og eller om bruger id'et er lig med brugerens id og returnere null hvis en eller begge er true
        if (wishlist == null || wishlist.getUserId() != userId) {
            return null;
        }
        return wishlist;
    }

}
