package dk.ek.wishlist.controller;

import dk.ek.wishlist.model.User;
import dk.ek.wishlist.service.WishService;
import dk.ek.wishlist.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WishlistController {

    private static final String SESSION_USER = "user";

    /// Initialisere WishlistService klassen som final
    private final WishlistService wishlistService;

    private final WishService wishService;

    /// Konstruktør
    // Wishservice bruges kun til at hente listens ønsker når en liste vises.
    public WishlistController(WishlistService wishlistService, WishService wishService){
        this.wishlistService = wishlistService;
        this.wishService = wishService;
    }

    /// Henter den indloggede bruger fra sessionen.
    private User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute(SESSION_USER);
    }


    @GetMapping("/lists")
    public String showLists(HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        // Vi bruger user.getId() fra sessionen og ikke et id fra URL'en,
        // så en bruger aldrig kan bede om en andens lister.
        model.addAttribute("lists", wishlistService.getUserLists(user.getId()));
        return "wishlists";
    }



    }
