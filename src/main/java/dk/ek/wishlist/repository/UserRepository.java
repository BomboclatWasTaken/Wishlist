package dk.ek.wishlist.repository;

import dk.ek.wishlist.model.User;

public interface UserRepository {

    void create (User user);

    User findById(int id);

    User findByEmail(String email);

    boolean emailExists(String email);
}
