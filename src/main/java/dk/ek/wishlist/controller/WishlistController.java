package dk.ek.wishlist.controller;

import dk.ek.wishlist.exception.WishlistException;
import dk.ek.wishlist.model.User;
import dk.ek.wishlist.model.Wishlist;
import dk.ek.wishlist.service.WishService;
import dk.ek.wishlist.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WishlistController {

    /// Det her er vores "Nøgle" hvori den indloggede bruger gemmes under i sessionen
    private static final String SESSION_USER = "user";

    /// Initialisere WishlistService & WishService klassen som final
    private final WishlistService wishlistService;
    private final WishService wishService;

    /// Konstruktør
    // Wishservice bruges kun til at hente listens ønsker når en liste vises.
    public WishlistController(WishlistService wishlistService, WishService wishService) {
        this.wishlistService = wishlistService;
        this.wishService = wishService;
    }

    /// Henter den indloggede bruger fra sessionen.
    private User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute(SESSION_USER);
    }

    /// Henter brugerens ønskelister
    @GetMapping("/wishlists")
    public String showLists(HttpSession session, Model model) {

        // Bruger metoden til at finde den indloggede bruger
        User user = getLoggedInUser(session);
        if (user == null) {
            // hvis den ikke finder nogen, returnere vi en redirect til loginsiden til brugeren.
            return "redirect:/login";
        }
        // Vi bruger user.getId() fra sessionen og ikke et id fra URL'en så en bruger ikke kan få adgang til en andens lister.
        model.addAttribute("wishlists", wishlistService.getUserLists(user.getId()));

        //Returnerer "brugerens-lister" siden
        return "wishlists";
    }
    @GetMapping("/wishlists/create")
    public String showCreateForm(HttpSession session) {
        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/login";
        }

        return "wishlist-form";
    }

    /// PostRequest til at lave en ny ønskeliste
    @PostMapping("/wishlists/create")
    public String createList(@RequestParam String title, HttpSession session) {

        // Bruger metoden til at finde den indloggede bruger
        User user = getLoggedInUser(session);
        if (user == null) {
            // hvis den ikke finder nogen, returnere vi en redirect til loginsiden til brugeren.
            return "redirect:/login";
        }

        // try/catch så vi slipper for whitelabel error, fx hvis titlen er tom
        try {
            // Servicen laver selv delekoden og gemmer listen.
            wishlistService.createWishlist(title, user.getId());
        } catch (WishlistException e) {
            return "redirect:/wishlists";
        }
        // Redirecter til "wishlists" siden
        return "redirect:/wishlists";
    }

    /// GetRequest til at vise ønskelister
    @GetMapping("/wishlists/{listId}")
    public String showList(@PathVariable int listId, HttpSession session, Model model) {

        // Bruger metoden til at finde den indloggede bruger
        User user = getLoggedInUser(session);
        if (user == null) {
            // hvis den ikke finder nogen, returnere vi en redirect til loginsiden til brugeren.
            return "redirect:/login";
        }

        // Servicen returnerer listen hvis den tilhører brugeren, uden tjekket kunne man skrive /lists/{en anden brugers liste} og få den frem.
        Wishlist wishlist = wishlistService.getSpecificList(listId, user.getId());
        if (wishlist == null) {
            return "redirect:/wishlists";
        }

        model.addAttribute("wishlist", wishlist);
        model.addAttribute("wishes", wishService.getWishes(listId));

        //Returnerer wishlists siden
        return "wishlist-detail";

    }

    /// PostRequest til at opdatere en ønskelistes titel
    @PostMapping("/wishlists/{listId}/edit")
    public String updateTitle(@PathVariable int listId, @RequestParam String title, HttpSession session) {

        // Bruger metoden til at finde den indloggede bruger
        User user = getLoggedInUser(session);
        if (user == null) {
            // hvis den ikke finder nogen, returnere vi en redirect til loginsiden til brugeren.
            return "redirect:/login";
        }

        try {
            wishlistService.updateTitle(listId, user.getId(), title);
        } catch (WishlistException e) {
            // Returnere wishlists siden hvis vi ikke kan finde listen vi vil opdatere
            return "redirect:/wishlists";
        }

        //returnere den specifikke listes side
        return "redirect:/wishlists/" + listId;
    }

    /// PostRequest til at slette en ønskeliste
    @PostMapping("/wishlists/{listId}/delete")
    public String deleteList(@PathVariable int listId, HttpSession session) {

        // Bruger metoden til at finde den indloggede bruger
        User user = getLoggedInUser(session);

        if (user == null) {
            // hvis den ikke finder nogen, returnere vi en redirect til loginsiden til brugeren.
            return "redirect:/login";
        }

        try {
            wishlistService.deleteWishlist(listId, user.getId());
        } catch (WishlistException e) {
            // Listen findes ikke, eller tilhører en anden bruger. Brugeren sendes alligevel tilbage til oversigten
        }
        return "redirect:/wishlists";
    }

}