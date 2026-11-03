package dk.ek.wishlist.service;

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
}
