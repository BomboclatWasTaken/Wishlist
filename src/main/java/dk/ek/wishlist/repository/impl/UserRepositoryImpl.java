package dk.ek.wishlist.repository.impl;

import dk.ek.wishlist.model.User;
import dk.ek.wishlist.repository.UserRepository;

public class UserRepositoryImpl implements UserRepository {

    public void create(User user){

    }

    public User findByEmail(String email){
      return null;
    }

    public User findById(int id){
        return null;
    }

    public boolean emailExists(String email) {
        return false;
    }
}
