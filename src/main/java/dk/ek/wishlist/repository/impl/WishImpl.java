package dk.ek.wishlist.repository.impl;

import dk.ek.wishlist.model.Wish;
import dk.ek.wishlist.repository.WishRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishImpl implements WishRepository {

    public void create(Wish wish) {

    }

    public List<Wish> findByWishlistId(int wishlistId){
        return null;
    }

    public Wish findById(int id){
        return null;
    }

    public void update(Wish wish){

    }

    public void delete(int id){

    }

    public void deleteByWishlistId(int wishlistId){

    }
}
