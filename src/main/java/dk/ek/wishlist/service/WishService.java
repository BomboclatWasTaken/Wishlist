package dk.ek.wishlist.service;

import dk.ek.wishlist.exception.*;
import dk.ek.wishlist.model.Wish;
import dk.ek.wishlist.model.Wishlist;
import dk.ek.wishlist.repository.WishRepository;
import dk.ek.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

        // Stopper, hvis listen ikke findes
        if (wishlist == null) {
            throw new WishlistNotFoundException(wish.getWishlistId());
        }

        // Stopper, hvis brugeren ikke ejer listen
        if (wishlist.getUserId() != userId) {
            throw new UnauthorizedAccessException();
        }

        validateWish(wish);

        wishRepository.create(wish);
    }

    /// Henter alle ønsker på en liste - bruges både af ejeren af listen og af gæster (den delte liste)
    public List<Wish> getWishes(int wishlistId) {
        return wishRepository.findByWishlistId(wishlistId);
    }
    /// Henter et ønske, hvis brugeren ejer listen. Kaster en exception, hvis det ikke findes eller ikke tilhører brugeren,
    /// og returnerer aldrig null.
    public Wish getWish(int wishId, int userId) {

        // Finder ønsket
        Wish wish = wishRepository.findById(wishId);
        if (wish == null) {
            throw new WishNotFoundException(wishId);
        }

        // Finder listen ønsket ligger på
        Wishlist wishlist = wishlistRepository.findById(wish.getWishlistId());
        if (wishlist == null) {
            throw new WishlistNotFoundException(wish.getWishlistId());
        }

        // Tjekker at brugeren ejer listen
        if (wishlist.getUserId() != userId) {
            throw new UnauthorizedAccessException();
        }

        return wish;
    }
    /// Opdaterer et ønske, hvis brugeren ejer listen. Ønsket bliver på sin oprindelige liste.
    public void updateWish(Wish wish, int userId) {

        // getWish kaster selv, hvis ønsket ikke findes eller ikke tilhører brugeren
        Wish existingWish = getWish(wish.getId(), userId);

        // Tjekker at de nye værdier er gyldige
        validateWish(wish);

        // Listen tages fra det gamle ønske, så ønsket ikke kan flyttes over på en andens liste
        wish.setWishlistId(existingWish.getWishlistId());

        wishRepository.update(wish);
    }
    /// Sletter et ønske, hvis brugeren ejer listen.
    public void deleteWish(int wishId, int userId) {

        // getWish kaster selv, hvis ønsket ikke findes eller ikke tilhører brugeren
        getWish(wishId, userId);

        wishRepository.delete(wishId);
    }
    ///Privat metode som kun bruges i wishService
    /// Tjekker at navnet ikke er tomt, og at prisen ikke er negativ
    private void validateWish(Wish wish) {

        // kasyter en exception hvis navnet mangler eller består kun af mellemrum
        if (wish.getName() == null || wish.getName().isBlank()) {
            throw new InvalidInputException("Wish name cannot be empty");
        }

        // kaster en exception hvis prisen er negativ
        if (wish.getPrice() < 0) {
            throw new InvalidInputException("Wish price cannot be negative");
        }
    }
}