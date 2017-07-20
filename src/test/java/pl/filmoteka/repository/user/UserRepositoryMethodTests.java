package pl.filmoteka.repository.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.User;
import pl.filmoteka.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for user repository.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryMethodTests {

    // Sample data
    private static final String username = "someusername";

    private static final String password = "pass";

    private static final String email = "foo@bar.com";

    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback
    public void testFindByLogin() {
        User user = new User(username, password, email);
        userRepository.saveAndFlush(user);
        User foundUser = userRepository.findByUsername(username);

        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    @Rollback
    public void testFindByLoginInvalid() {
        User user = new User(username, password, email);
        userRepository.saveAndFlush(user);
        User foundUser = userRepository.findByUsername("dummy");

        assertThat(foundUser).isNull();
    }
}
