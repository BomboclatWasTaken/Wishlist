package dk.ek.wishlist.repository.impl;

import dk.ek.wishlist.model.Wish;
import dk.ek.wishlist.repository.WishRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WishRepositoryImpl implements WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishRepositoryImpl (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /// Gemmer et nyt ønske på en liste.
    public void create(Wish wish) {

        //Skriver til databasen at den skal indsætte et nyt ønske ind i "wish" - tabellen med de følgende data.
        //Værdierne sendes via ? ved hjælp af jdbcTemplate.update
        String sql = "INSERT INTO wish (name, description, price, link_url, picture_url, wish_list_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, wish.getName(), wish.getDescription(), wish.getPrice(), wish.getLinkUrl(), wish.getPictureUrl(), wish.getWishlistId());


    }
    /// Finder et ønske ud fra id, returnerer null hvis det id ikke findes.
    @Override
    public Wish findById(int id){

        //Skriver til databasen at vi skal hente præcis det ene ønske med id'et vi indtaster
        String sql = "SELECT * FROM wish WHERE id = ?";

        //Opretter listen wishes og putter det ene ønske med vores indtastede id ind i listen wishes
        List<Wish> wishes = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Wish.class), id);

        //Hvis listen er tom, skal den returnere null
        if (wishes.isEmpty()){
            return null;

        }
        //Kan overvejes at bruge wishes.getFirst()?
        return wishes.get(0);
    }


    /// Henter alle ønsker på fra en liste.
    @Override
    public List<Wish> findByWishlistId(int wishlistId){

        //Skriver til databasen at vi skal hente alle ønsker der har det indtastede ønskeliste id.
        String sql = "SELECT * FROM wish WHERE wish_list_id = ?";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Wish.class), wishlistId);
    }

    /// Opdaterer et ønske
    @Override
    public void update(Wish wish){

        //Skriver til databasen at vi skal opdatere følgende data og sørger for id'et forbliver det samme.
        String sql = "UPDATE wish SET name = ?, description = ?, price = ?, link_url = ?, picture_url = ? WHERE id = ?";

        jdbcTemplate.update(sql, wish.getName(), wish.getDescription(), wish.getPrice(), wish.getLinkUrl(), wish.getPictureUrl(), wish.getId());
    }

    /// Sletter et ønske
    public void delete(int id){
        //Skriver til databasen at den skal slette ønsket med netop det id der er indtastet.
        String sql = "DELETE FROM wish WHERE id = ?";

        jdbcTemplate.update(sql,id);

    }

    /// Sletter alle ønsker på en liste
    public void deleteByWishlistId(int wishlistId){

        //Skriver til databasen at vi skal slette de ønsker fra "wish" - tabellen, hvor ønskelistens id er det indtastede.
        String sql = "DELETE FROM wish WHERE wish_list_id = ?";

        jdbcTemplate.update(sql, wishlistId);

    }
}
