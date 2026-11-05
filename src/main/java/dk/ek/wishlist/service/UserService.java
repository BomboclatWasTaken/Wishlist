package dk.ek.wishlist.service;

import dk.ek.wishlist.exception.EmailAlreadyExistsException;
import dk.ek.wishlist.exception.InvalidInputException;
import dk.ek.wishlist.exception.InvalidLoginException;
import dk.ek.wishlist.exception.UserNotFoundException;
import dk.ek.wishlist.model.User;
import dk.ek.wishlist.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    /// Initialisere UserRepository klassen som final
    private final UserRepository userRepository;

    /// Konstruktør
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /// Opretter ny bruger. Kaster en exception, hvis input er ugyldigt eller emailen allerede er i brug.
    public void register(User user) {

        // Tjekker at navn, email og kodeord er udfyldt og gyldige
        validateUser(user);

        // Hvis emailen allerede er i brug, kaster vi en exception til controlleren,
        // som fanger den og viser fejlen på hjemmesiden
        if (userRepository.emailExists(user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        // Emailen er ledig, så vi opretter brugeren
        userRepository.create(user);
    }


    /// Tjekker login. Returnerer brugeren, hvis email og kodeord er rigtige, ellers kastes en exception.
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);

        // Hvis emailen ikke findes, eller kodeordet er forkert, kaster vi den samme exception i begge tilfælde
        // (Så kan man ikke se, om det er emailen eller kodeordet der er forkert)
        // (user == null tjekkes først, så vi ikke kalder getPassword() på en bruger der ikke findes)
        if (user == null || !user.getPassword().equals(password)) {
            throw new InvalidLoginException();
        }

        // Email og kodeord passer, så vi returnerer brugeren
        return user;
    }

    /// Henter en bruger ud fra id. Returnerer aldrig null, men kaster en exception, hvis brugeren ikke findes.
    public User getById(int id) {
        User user = userRepository.findById(id);

        // Hvis brugeren ikke findes i databasen, kaster vi en exception
        if (user == null) {
            throw new UserNotFoundException(id);
        }

        return user;
    }

    /// Privat metode som kun bruges i UserService. Tjekker at navn, email og kodeord er gyldige.
    private void validateUser(User user) {

        // Navnet mangler (null) eller består kun af mellemrum (isBlank)
        if (user.getName() == null || user.getName().isBlank()) {
            throw new InvalidInputException("Name cannot be empty");
        }

        // Emailen mangler eller ligner ikke en email (skal indeholde @)
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new InvalidInputException("Email is not valid");
        }

        // Kodeordet mangler eller består kun af mellemrum
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new InvalidInputException("Password cannot be empty");
        }
    }
}
