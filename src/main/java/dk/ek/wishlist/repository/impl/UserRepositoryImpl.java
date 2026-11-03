package dk.ek.wishlist.repository.impl;

import dk.ek.wishlist.model.User;
import dk.ek.wishlist.repository.UserRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /// Opretter en bruger
    public void create(User user){

        // Opretter variabel der indeholder vores sql streng
        // Strengen fortæller databasen at den skal indsætte en ny række i "users" - tabellen med værdier til kolonnerne name, email og password.
        // De tre ? er pladsholdere der bliver erstattet af værdierne i jdbcTemplate.update kaldet.
        String sql = "INSERT INTO users (name, email, password VALUES (?, ?, ?)";

        //jdbcTemplate.update udfører SQL'en og erstatter ? med værdierne fra wishlist-objektet i rækkefølge
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword());

    }

    /// Finder en bruger ved brug af email
    public User findByEmail(String email) {

        // Opretter variabel der indeholder vores sql streng
        // Strengen fortæller databasen at den skal vælge alt fra users hvor email er lig ? (pladsholer)
        String sql = "SELECT * FROM users WHERE email = ?";

        // Opretter en variabel der kan indeholde en liste af User-objekter og sætter den lig jdbcTemplate.query som udfører SQL'en mod vores database
        // BeanPropertyRowMapper<>(User.class) fortæller spring hvordan hver række i resultatet skal laves om til et User-objekt
        // Altså matcher den kolonnenavnene i databasen med vores field names i klassen, email til sidst erstatter ? i vores SQL query
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);

        // Hvis listen er tom returnere vi null
        if (users.isEmpty()){
            return null;
        }

        // ellers returneres objektet på plads 0 i listen - altså det objekt med vores indtastede email (Overvej her igen users.getFirst()).
      return users.get(0);
    }

    /// Finder en bruger ved brug af id
    public User findById(int id){

        // Opretter variabel der indeholder vores sql streng
        // Strengen fortæller databasen at den skal vælge alt fra users hvor id er lig ? (pladsholer)
        String sql = "SELECT * FROM users WHERE id = ?";

        // Opretter en variabel der kan indeholde en liste af User-objekter og sætter den lig jdbcTemplate.query som udfører SQL'en mod vores database
        // BeanPropertyRowMapper<>(User.class) fortæller spring hvordan hver række i resultatet skal laves om til et User-objekt
        // Altså matcher den kolonnenavnene i databasen med vores field names i klassen, id til sidst erstatter ? i vores SQL query
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class) , id);

        // Hvis listen er tom returnere vi null
        if (users.isEmpty()){
            return null;
        }

        // ellers returneres objektet på plads 0 i listen - altså det objekt med vores indtastede id (Overvej her igen users.getFirst()).
        return users.get(0);
    }

    /// Tjekker om emailen allerede er i brug
    public boolean emailExists(String email) {
        // Return statement der bruger metoden findByEmail() med vores indtastede email til at returnere true hvis den finder en bruger med samme email
        // ellers returneres der false
        return findByEmail(email) != null;
    }
}
