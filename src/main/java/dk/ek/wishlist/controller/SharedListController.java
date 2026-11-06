package dk.ek.wishlist.controller;

import dk.ek.wishlist.exception.ShareCodeNotFoundException;
import dk.ek.wishlist.model.Wishlist;
import dk.ek.wishlist.service.WishService;
import dk.ek.wishlist.service.WishlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
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

    @GetMapping("/shared/{shareCode}")
    public String showSharedList(@PathVariable String shareCode, Model model) {

        // Servicen kaster en exception, hvis ingen liste har den delingskode
        Wishlist wishlist;
        try {
            wishlist = wishlistService.getSharedList(shareCode);
        } catch (ShareCodeNotFoundException e) {
            model.addAttribute("errorMessage", "Den delte ønskeliste findes ikke.");
            return "shared-list";
        }

        model.addAttribute("wishlist", wishlist);
        model.addAttribute("wishes", wishService.getWishes(wishlist.getId()));

        return "shared-list";
    }
}
