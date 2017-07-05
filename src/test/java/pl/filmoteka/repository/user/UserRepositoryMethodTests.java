package pl.filmoteka.repository.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.User;
import pl.filmoteka.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for user repository.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryMethodTests {

    // Sample data
    private static final String login = "somelogin";

    private static final String password = "pass";

    private static final String email = "foo@bar.com";

    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback
    public void testFindByLogin() {
        User user = new User(login, password, email);
        userRepository.saveAndFlush(user);
        List<User> foundUsers = userRepository.findByLogin(login);

        assertThat(foundUsers).contains(user).hasSize(1);
    }

    @Test
    @Rollback
    public void testFindByLoginInvalid() {
        User user = new User(login, password, email);
        userRepository.saveAndFlush(user);
        List<User> foundUsers = userRepository.findByLogin("dummy");

        assertThat(foundUsers).doesNotContain(user).hasSize(0);
    }
}
