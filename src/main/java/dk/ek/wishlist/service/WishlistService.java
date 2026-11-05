package dk.ek.wishlist.service;

import dk.ek.wishlist.exception.InvalidInputException;
import dk.ek.wishlist.exception.ShareCodeNotFoundException;
import dk.ek.wishlist.exception.UnauthorizedAccessException;
import dk.ek.wishlist.exception.WishlistNotFoundException;
import dk.ek.wishlist.model.Wishlist;
import dk.ek.wishlist.repository.WishRepository;
import dk.ek.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class WishlistService {

    /// Initialisere WishlistRepository klassen som final
    private final WishlistRepository wishlistRepository;
    private final WishRepository wishRepository;

    /// Konstruktør
    public WishlistService(WishlistRepository wishlistRepository, WishRepository wishRepository){
        this.wishlistRepository = wishlistRepository;
        this.wishRepository = wishRepository;
    }

    /// Henter en specifik liste, hvis den tilhører brugeren. Returnerer aldrig null, men kaster en exception hvis noget er galt
    public Wishlist getSpecificList(int listId, int userId) {
        Wishlist wishlist = wishlistRepository.findById(listId);

        // Hvis listen ikke findes i databasen, kaster vi en exception til controlleren
        // (Vi tjekker null først, så vi ikke kalder getUserId() på en liste der ikke findes)
        if (wishlist == null) {
            throw new WishlistNotFoundException(listId);
        }
        // Hvis listen findes, men tilhører en anden bruger, må brugeren ikke se den
        if (wishlist.getUserId() != userId){
            throw new UnauthorizedAccessException();
        }
        // Listen findes og tilhører brugeren, så vi returnerer den
        return wishlist;
    }

    /// Opretter en ny liste til brugeren med en unik delingskode.
    public void createWishlist(Wishlist wishlist, int userId) {

        // Tjekker at titlen ikke er tom, ellers kaster validateWishlist en exception
        validateWishlist(wishlist);

        // Listen sættes altid til den indloggede bruger, så man ikke kan oprette lister på andres konti
        wishlist.setUserId(userId);

        // Laver en tilfældig delingskode, som gæster kan bruge til at se listen
        wishlist.setShareCode(generateShareCode());

        wishlistRepository.create(wishlist);
    }

    /// Henter alle lister der tilhører brugeren. Giver en tom liste, hvis brugeren ingen har.
    public List<Wishlist> getUserLists(int userId) {
        return wishlistRepository.findByUserId(userId);
    }

    /// Henter en delt liste ud fra delingskoden. Bruges af gæster, så der tjekkes ikke ejerskab.
    public Wishlist getSharedList(String shareCode) {
        Wishlist wishlist = wishlistRepository.findByShareCode(shareCode);

        // Hvis ingen liste har den delingskode, kaster vi en exception til controlleren
        if (wishlist == null) {
            throw new ShareCodeNotFoundException(shareCode);
        }
        return wishlist;
    }

    /// Ændrer titlen på en liste, hvis brugeren ejer den.
    public void updateWishlist(Wishlist wishlist, int userId) {

        // getSpecificList kaster selv, hvis listen ikke findes eller ikke tilhører brugeren
        getSpecificList(wishlist.getId(), userId);

        // Tjekker at den nye titel ikke er tom
        validateWishlist(wishlist);

        wishlistRepository.update(wishlist);
    }

    /// Sletter en liste og dens ønsker, hvis brugeren ejer listen.
    public void deleteWishlist(int listId, int userId) {

        // getSpecificList kaster selv, hvis listen ikke findes eller ikke tilhører brugeren
        getSpecificList(listId, userId);

        // Ønskerne på listen slettes først, ellers kan databasen afvise at slette listen
        // (Kan fjernes, hvis databasen har ON DELETE CASCADE)
        wishRepository.deleteByWishlistId(listId);

        // Når ønskerne er væk, kan selve listen slettes
        wishlistRepository.delete(listId);
    }

    /// Privat metode som kun bruges i WishlistService. Tjekker at titlen ikke er tom.
    private void validateWishlist(Wishlist wishlist) {

        // Titlen mangler (null) eller består kun af mellemrum (isBlank)
        // (null tjekkes først, så isBlank() aldrig kaldes på null)
        if (wishlist.getTitle() == null || wishlist.getTitle().isBlank()) {
            throw new InvalidInputException("Wishlist title cannot be empty");
        }
    }

    /// Privat metode der laver en tilfældig delingskode på 6 cifre og laver en ny, hvis den allerede findes.
    private String generateShareCode() {

        // Random er Javas tilfældighedsgenerator
        Random random = new Random();

        // nextInt(900000) giver et tal fra 0 til 899999, og + 100000 gør at tallet altid har 6 cifre
        // String.valueOf laver tallet om til tekst, fordi delingskoden gemmes som en String
        String code = String.valueOf(random.nextInt(900000) + 100000);

        // Hvis en anden liste allerede har koden, laver vi en ny, indtil vi finder en der er ledig
        while (wishlistRepository.findByShareCode(code) != null) {
            code = String.valueOf(random.nextInt(900000) + 100000);
        }

        return code;
    }
}
