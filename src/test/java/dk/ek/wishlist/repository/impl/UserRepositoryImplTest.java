package dk.ek.wishlist.repository.impl;
import dk.ek.wishlist.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
// Bruger "test"-profile, så testen kører mod H2-databasen i hukommelsen og ikke den rigtige database
@ActiveProfiles("test")
// Kører h2init.sql før hver test: sletter og laver tabellerne igen
//Starter alle tests med de samme data, og en test kan ikke påvirke de andre.
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
class UserRepositoryImplTest {



    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    void findByEmail_whenEmailExists_returnsUser() {

        User user = userRepository.findByEmail("anna@test.dk");

        assertThat(user).isNotNull();
        //Navnet og id'et skal matche det der står i h2init.sql
        assertThat(user.getName()).isEqualTo("Anna");
        assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    void findByEmail_whenEmailDoesNotExist_returnsNull() {

        User user = userRepository.findByEmail("findes@ikke.dk");

        assertThat(user).isNull();
    }

    @Test
    void findById_whenIdExists_returnsUser() {
        User user = userRepository.findById(2);

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("Mads");
        assertThat(user.getEmail()).isEqualTo("mads@test.dk");
    }

    @Test
    void emailExists_returnsTrueForKnownEmailAndFalseForUnknownEmail() {
        assertThat(userRepository.emailExists("anna@test.dk")).isTrue();
        assertThat(userRepository.emailExists("findes@ikke.dk")).isFalse();
    }

    @Test
    void create_savesNewUserSoItCanBeFoundAgain() {
        User newUser = new User(0, "Carl", "carl@test.dk", "abcd");

        userRepository.create(newUser);

        User found = userRepository.findByEmail("carl@test.dk");
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Carl");
    }

}