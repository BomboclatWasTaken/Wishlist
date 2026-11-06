package dk.ek.wishlist.repository.impl;

import dk.ek.wishlist.model.Wishlist;
import dk.ek.wishlist.repository.WishlistRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

// Annotationen @Repository fortæller spring at denne klasse er et repository.
@Repository
public class WishlistRepositoryImpl implements WishlistRepository {

    // Initialisere en JdbcTemplate og kalder den jdbcTemplate.
    private final JdbcTemplate jdbcTemplate;

    /// Konstruktør
    public WishlistRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Wishlist wishlist) {

        // Opretter en variabel der indeholder vores sql streng
        // Strengen fortæller databasen at den skal oprette en ny række i "wish_list" - tabellen med værdier til kolonnerne title, share_code og user_id
        // De tre ? er pladsholdere, som bliver erstattet af værdierne i jdbcTemplate.update kaldet
        String sql = "INSERT INTO wish_list (title, share_code, user_id) VALUES (?, ?, ?)";

        //jdbcTemplate.update udfører SQL'en og erstatter ? med værdierne fra wishlist-objektet i rækkefølge
        jdbcTemplate.update(sql, wishlist.getTitle(), wishlist.getShareCode(), wishlist.getUserId());

    }

    /// Finder en liste ud fra id
    public Wishlist findById(int id){

        // Opretter variabel der indeholder vores sql streng
        // Strengen fortæller databasen at den skal vælge alt fra "wish_list" - tabellen hvor id'et er lig ?
        // (Senere erstattes dette med det indtastede id med jdbcTemplate.query.)
        String sql = "SELECT * FROM wish_list WHERE id = ?";

        // Opretter en variabel der kan indeholde en liste af Wishlist-objekter og sætter den lig jdbcTemplate.query som udfører SQL'en mod vores database
        // BeanPropertyRowMapper<>(Wishlist.class) fortæller spring hvordan hver række i resultatet skal laves om til et Wishlist-objekt
        // Altså matcher den kolonnenavnene i databasen med vores field names i klassen, id til sidst erstatter ? i vores SQL query
        List<Wishlist> lists = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Wishlist.class), id);

        //Hvis listen er tom, returnerer vi null
        if (lists.isEmpty()) {
            return null;
        }

        //Returnere dataen på plads 0 i listen - altså det object med vores indtastede id (overvej igen her lists.getFirst())
        return lists.get(0);
    }


    /// Henter alle lister der tilhører en bruger
    public List<Wishlist> findByUserId(int userId){

        //Opretter variabel der indeholder vores sql streng
        //Strengen fortæller databasen at den skal vælge alt fra "wish_list" - tabellen hvor user_id er lig ?
        //(Senere erstattes dette med vores indtastede id ved hjælp af jdbcTemplate.query
        String sql = "SELECT * FROM wish_list WHERE user_id = ?";

        //Returnerer en liste af objekter der har fået data fra databasen og indsat på deres korrekte field names i objekterne ved hjælp af BeanPropertyRowMapper.
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Wishlist.class), userId);
    }



    /// Finder en liste ved hjælp af en delekode
    public Wishlist findByShareCode(String shareCode){

        // Opretter variabel der indeholder vores sql streng
        // sql strengen fortæller databasen at den skal finde alt fr "wish_list" - tabellen hvor sharecode er lig ?
        // ? er en placeholder der erstattes ved hjælp af jdbcTemplate.query
        String sql = "SELECT * FROM wish_list WHERE share_code = ?";

        // Opretter en liste der kan indeholde Wishlist objekter og sætter den lig vores jdbcTemplate.query
        // jdbcTemplate.query kører vores sql streng og erstatter placeholderen ? med den indtastede shareCode
        List<Wishlist> lists = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Wishlist.class), shareCode);

        // Hvis listen er tom, returner null
        if (lists.isEmpty()) {
            return null;
        }

        // Hvis den ikke er tom, skal den returnere det objekt der er på plads 0 i listen (Overvej her lists.getFirst() igen)
        return lists.get(0);
    }

    /// Ændre titlen på en liste
    public void update(Wishlist wishlist){

        // Opretter en variabel til vores SQL streng
        // Strengen fortæller databasen at den skal opdatere "wish_list" - tabellen og sætte titlen lig ? (pladsholder) hvor id er lig ? (pladsholder)
        String sql = "UPDATE wish_list SET title = ? WHERE id = ?";

        //jdbcTemplate.update udfører SQL'en og erstatter ? med værdierne fra wishlist-objektet i rækkefølge
        jdbcTemplate.update(sql, wishlist.getTitle(), wishlist.getId());
    }

    /// Sletter en liste
    public void delete(int id){

        // Opretter variabel der indeholder vores sql streng
        // SQL strengen fortæller databsen at den skal slette fra "wish_list" - tabellen hvor id er lig ? (pladsholder)
        String sql = "DELETE FROM wish_list WHERE id = ?";

        //jdbcTemplate.update udfører SQL'en og erstatter ? med værdien af det indtastede id.
        jdbcTemplate.update(sql, id);
    }
}
