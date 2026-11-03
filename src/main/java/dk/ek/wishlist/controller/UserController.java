package dk.ek.wishlist.controller;

import dk.ek.wishlist.service.UserService;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    /// Initialisere UserService klassen som final
    private final UserService userService;

    /// Konstruktør
    public UserController(UserService userService){
        this.userService = userService;
    }
}
