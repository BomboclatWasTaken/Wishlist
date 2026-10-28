package dk.ek.wishlist.repository;

import dk.ek.wishlist.model.Wishlist;

import java.util.List;

public interface WishlistRepository extends CrudRepository<Wishlist> {

    List<Wishlist> findByUserId(int UserId);

    Wishlist findByShareCode(String shareCode);
}
