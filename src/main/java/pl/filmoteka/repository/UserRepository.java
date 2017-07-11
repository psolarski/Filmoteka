package pl.filmoteka.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.filmoteka.model.User;

import java.util.List;

/**
 * Repository class for User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByLogin(String login);

    List<User> findByEmail(String email);

    @EntityGraph("graph.User.movies")
    List<User> findAll();
}
