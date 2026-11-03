package dk.ek.wishlist.service;

import dk.ek.wishlist.exception.UserNotFoundException;
import dk.ek.wishlist.exception.WishlistNotFoundException;
import dk.ek.wishlist.model.Wish;
import dk.ek.wishlist.model.Wishlist;
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

    public void addWish(Wish wish, int userId) {

        Wishlist wishlist = wishlistRepository.findById(wish.getWishlistId());

        if (wishlist == null) {
            throw new WishlistNotFoundException(wish.getWishlistId());
        }
        if (wishlist.getUserId() !=userId) {
            throw new UserNotFoundException(userId);
        }

        wishRepository.create(wish);
    }
}
