package dk.ek.wishlist.repository;

import dk.ek.wishlist.model.Wish;

import java.util.List;

public interface WishRepository extends CrudRepository<Wish> {

    List<Wish> findByWishlistId(int wishlistId);

    void deleteByWishlistId(int wishlistId);
}
