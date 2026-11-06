package dk.ek.wishlist.controller;

import dk.ek.wishlist.exception.EmailAlreadyExistsException;
import dk.ek.wishlist.exception.InvalidInputException;
import dk.ek.wishlist.exception.InvalidLoginException;
import dk.ek.wishlist.model.User;
import dk.ek.wishlist.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {

    /// Initialisere UserService klassen som final
    private final UserService userService;

    private static final String SESSION_USER = "user";

    /// Konstruktør
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /// Getrequest til forsiden
    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    /// Getrequest til register siden
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


    /// Postrequest til register
    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {

        //Kalder på servicens metode register og forsøger at oprette bruger
        try {
            userService.register(user);

            // Emailen er i brug, eller input er ugyldigt (fx tomt navn): vi viser siden igen med fejlbeskeden
            // (Med | fanger vi to exceptions med samme håndtering)
        } catch (EmailAlreadyExistsException | InvalidInputException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
        // Redirecter til login siden
        return "redirect:/login";
    }

    /// Getrequest til login
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    /// Postrequest til loging
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {

        // Servicen returnerer brugeren hvis login er korrekt, ellers kaster den en exception
        User user;
        try {
            user = userService.login(email, password);

        } catch (InvalidLoginException e) {
            // Forkert email eller kodeord: brugeren får en fejlbesked
            model.addAttribute("errorMessage", "Forkert e-mail eller kodeord");
            return "login";
        }

        // Gem brugeren i sessionen, så vi kan genkende ham ved næste request.
        session.setAttribute(SESSION_USER, user);
        return "redirect:/lists";
    }

        /// Getrequest til logout
        @GetMapping("/logout")
        public String logout (HttpSession session){

            //Session.invalidate() invalidere sessionen og brugeren logges ud
            session.invalidate();

            //Redirecter til forsiden
            return "redirect:/";
        }
    }
