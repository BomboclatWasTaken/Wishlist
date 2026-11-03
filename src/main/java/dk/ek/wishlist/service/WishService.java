package dk.ek.wishlist.service;

import dk.ek.wishlist.exception.UserNotFoundException;
import dk.ek.wishlist.exception.WishlistNotFoundException;
import dk.ek.wishlist.model.Wish;
import dk.ek.wishlist.model.Wishlist;
import dk.ek.wishlist.repository.WishRepository;
import dk.ek.wishlist.repository.WishlistRepository;

import java.util.List;

public class WishService {

    /// Initialisere WishlistRepository som final
    private final WishlistRepository wishlistRepository;

    /// Initialisere WishRepository som final
    private final WishRepository wishRepository;

    /// Konstruktør
    public WishService(WishlistRepository wishlistRepository, WishRepository wishRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishRepository = wishRepository;
    }

    ///  Tilføjer et ønske hvis listen tilhører brugeren
    public void addWish(Wish wish, int userId) {

        Wishlist wishlist = wishlistRepository.findById(wish.getWishlistId());

        if (wishlist == null) {
            throw new WishlistNotFoundException(wish.getWishlistId());
        }
        if (wishlist.getUserId() != userId) {
            throw new UserNotFoundException(userId);
        }

        wishRepository.create(wish);
    }

    /// Henter alle ønsker på en liste - bruges både af ejeren af listen og af gæster (den delte liste)
    public List<Wish> getWishes(int wishlistId) {
        return wishRepository.findByWishlistId(wishlistId);
    }

    /// Henter et ønske, kun hvis brugeren ejer listen ønsket ligger på
    public Wish getWish(int wishId, int userId) {
        Wish wish = wishRepository.findById(wishId);

        if (wish == null) {
            return null;
        }

        Wishlist wishlist = wishlistRepository.findById(wish.getWishlistId());

        if (wishlist == null || wishlist.getUserId() != userId) {
            return null;
        }
        return wish;
    }

    /// Opdaterer et ønske hvis det tilhører brugeren
    public void updateWish(Wish wish, int userId) {
        Wish existingWish = getWish(wish.getId(), userId);

        if (existingWish == null) {
            throw new IllegalArgumentException("Indsæt custom exception her");
        }

        wishRepository.update(wish);
    }

    /// Sletter et ønske hvis det tilhører brugeren
    public void deleteWish(int wishId, int userId) {
        Wish wish = getWish(wishId, userId);

        if (wish == null) {
            throw new IllegalArgumentException("Indsæt custom exception her");
        }

        wishRepository.delete(wishId);
    }
}