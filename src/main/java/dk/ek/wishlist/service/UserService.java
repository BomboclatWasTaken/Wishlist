package dk.ek.wishlist.service;

import dk.ek.wishlist.model.User;
import dk.ek.wishlist.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    /// Initialisere UserRepository klassen som final
    private final UserRepository userRepository;

    ///Konstruktør
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /// Opretter ny bruger
    public void register(User user) {

        // Hvis brugeren allerede eksistere kaster vi en exception til controlleren som fanger den og viser fejl på hjemmesiden
        // (Husk at indsætte vores custom exception)
        if (userRepository.emailExists(user.getEmail())) {

            throw new IllegalArgumentException("Custom exception indsættes her");
        }
        // Hvis brugeren ikke allerede eksistere opretter vi brugeren.
        userRepository.create(user);
    }


    ///  Tjekker login
    public User login (String email, String password) {
        User user = userRepository.findByEmail(email);

        // returnere brugeren hvis email og password er korrekt
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        // ellers returneres der null
        return null;
    }

    ///  Henter en bruger ud fra id, returnere null hvis brugeren ikke findes.
    public User getById(int id) {
        return userRepository.findById(id);
    }
}
