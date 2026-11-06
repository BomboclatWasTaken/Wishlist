package dk.ek.wishlist.repository.impl;
import dk.ek.wishlist.model.Wish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
class WishRepositoryImplTest {

    @Autowired
    private WishRepositoryImpl wishRepository;

        @Test
    void findById_whenIdDoesNotExist_returnsNull() {
        assertThat(wishRepository.findById(67)).isNull();
    }

    @Test
    void findByWishlistId_returnsAllWishesOnTheList() {
        List<Wish> list1 = wishRepository.findByWishlistId(1);
        List<Wish> list2 = wishRepository.findByWishlistId(2);

        assertThat(list1.size()).isEqualTo(3);
        assertThat(list2.size()).isEqualTo(2);
    }

    @Test
    void create_addWishToTheList() {
        Wish newWish = new Wish(2,"Movie","A bad one", 120.00,null, null, 3);
        wishRepository.create(newWish);

        List<Wish> wishes = wishRepository.findByWishlistId(3);
        assertThat(wishes.size()).isEqualTo(3);
    }

    @Test
    void update_changesNameAndPrice() {
        Wish wish = wishRepository.findById(1);
        wish.setName("Black headset");
        wish.setPrice(600.00);
        wishRepository.update(wish);
    }

    @Test
    void delete_removesOnlyThatWish() {
        wishRepository.delete(1);

        assertThat(wishRepository.findById(1)).isNull();
        assertThat(wishRepository.findByWishlistId(1).size()).isEqualTo(2);
    }

    @Test
    void deleteByWishlistId_removesAllWishesOnThatList() {
        wishRepository.deleteByWishlistId(2);

        assertThat(wishRepository.findByWishlistId(2)).isEmpty();
        assertThat(wishRepository.findByWishlistId(1).size()).isEqualTo(3);
    }
}