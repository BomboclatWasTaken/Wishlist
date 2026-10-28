package dk.ek.wishlist.repository;

public interface CrudRepository <T> {

    void create(T entity);

    T findById(int id);

    void update(T entity);

    void delete(int id);
}
