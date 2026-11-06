package dk.ek.wishlist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test") // Brug H2 og ikke dev-profilen, der kræver miljøvariabler
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD) // Opret tabellerne i H2
class WishlistApplicationTests {

    @Test
    void contextLoads() {
    }

}