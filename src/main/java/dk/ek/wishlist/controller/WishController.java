package dk.ek.wishlist.controller;

import dk.ek.wishlist.model.User;
import dk.ek.wishlist.model.Wish;
import dk.ek.wishlist.service.WishService;
import dk.ek.wishlist.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WishController {

    // Variabel til at gemme brugeren i sessionen
    private static final String SESSION_USER = "user";

    // Initialisere WishService klassen som final
    private final WishService wishService;

    // Initialisere WishlistService klassen som final
    private final WishlistService wishlistService;

    /// Konstruktør
    public WishController(WishService wishService, WishlistService wishlistService){
        this.wishService = wishService;
        this.wishlistService = wishlistService;
    }

    // Metode til at hente den indloggede bruger fra sessionen - returnere null hvis der ikke er nogen
    private User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute(SESSION_USER);
    }

    /// Get request til nyt ønske (Viser siden til at tilføje nyt ønske)
    @GetMapping("/wishlists/{listId}/wishes/new")
    public String showNewForm(@PathVariable int listId, HttpSession session, Model model) {

        // Bruger metoden til at finde den indloggede bruger
        User user = getLoggedInUser(session);
        if (user == null) {
            // hvis den ikke finder nogen, returnere vi en redirect til loginsiden til brugeren.
            return "redirect:/login";
        }

        // Tjekker om listen findes og om den tilhører brugeren
        if (wishlistService.getSpecificList(listId, user.getId()) == null) {
            return "redirect:/wishlists";
        }

        // Tomt Wish-objekt, så formularen (th:object) har noget at binde til.
        model.addAttribute("wish", new Wish());

        // listId sendes med, så formularens action kan pege på den rigtige URL.
        model.addAttribute("listId", listId);

        // Returnere html siden
        return "wish-form";
    }

    /// PostRequest der gemmer det nye ønske
    @PostMapping("/wishlists/{listId}/wishes")
    public String addWish(@PathVariable int listId, @ModelAttribute Wish wish, HttpSession session) {

        // Bruger metoden til at finde den indloggede bruger
        User user = getLoggedInUser(session);
        if (user == null) {
            // hvis den ikke finder nogen, returnere vi en redirect til loginsiden til brugeren.
            return "redirect:/login";
        }

        // Sikkerhed: listen sættes ALTID fra URL'en og ikke fra formularen.
        // Ellers kunne en bruger rette et skjult felt og lægge ønsker på andres lister.
        wish.setWishlistId(listId);

        // try/catch så vi slipper for whitelabel error
        try {
            // Servicen tjekker, at listen tilhører brugeren, og gemmer ønsket.
            wishService.addWish(wish, user.getId());
        } catch (IllegalArgumentException e) {
            return "redirect:/wishlists";
        }
        return "redirect:/wishlists/" + listId;
    }

    ///  Getrequest til at opdatere et ønske
    @GetMapping("/wishlists/{listId}/wishes/{wishId}/edit")
    public String showEditForm(@PathVariable int listId, @PathVariable int wishId, HttpSession session, Model model) {

        // Bruger metoden til at finde den indloggede bruger
        User user = getLoggedInUser(session);

        // hvis den ikke finder nogen, returnere vi en redirect til loginsiden til brugeren
        if (user == null) {
            return "redirect:/login";
        }
        // Servicen returnerer kun ønsket hvis det ligger på en liste brugeren ejer
        Wish wish = wishService.getWish(wishId, user.getId());
        if (wish == null) {
            return "redirect:/wishlists/" + listId;
        }

        // Wish-objektet forudfylder formularen via th:object og th:field
        model.addAttribute("wish", wish);
        model.addAttribute("listId", listId);
        return "wish-edit";
    }

    /// Postrequest til at opdatere et ønske
    @PostMapping("/wishlists/{listId}/wishes/{wishId}/edit")
    public String updateWish(@PathVariable int listId, @PathVariable int wishId, @ModelAttribute Wish wish, HttpSession session) {

        // Bruger metoden til at finde den indloggede bruger
        User user = getLoggedInUser(session);

        // hvis den ikke finder nogen, returnere vi en redirect til loginsiden til brugeren
        if (user == null) {
            return "redirect:/login";
        }

        // Id'erne sættes fra URL'en og ikke fra formularen, så en bruger ikke kan
        // redigere et andet ønske ved at ændre et skjult felt.
        wish.setId(wishId);
        wish.setWishlistId(listId);

        // try/catch så vi slipper for whitelabel error
        try {
            wishService.updateWish(wish, user.getId());
        } catch (IllegalArgumentException e) {
            return "redirect:/wishlists";
        }
        return "redirect:/wishlists/" + listId;
    }
    /// Postrequest til at slette et ønske
    @PostMapping("/wishlists/{listId}/wishes/{wishId}/delete")
    public String deleteWish(@PathVariable int listId, @PathVariable int wishId, HttpSession session) {

        // Bruger metoden til at finde den indloggede bruger
        User user = getLoggedInUser(session);

        // hvis den ikke finder nogen, returnere vi en redirect til loginsiden til brugeren
        if (user == null) {
            return "redirect:/login";
        }

        // try/catch så vi slipper for whitelabel error
        try {
            wishService.deleteWish(wishId, user.getId());
        } catch (IllegalArgumentException e) {
            return "redirect:/wishlists";
        }

        return "redirect:/wishlists/" + listId;
    }
}
