package pl.filmoteka.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.filmoteka.model.User;

import java.util.List;

/**
 * Repository class for user entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    List<User> findByEmail(String email);

    @EntityGraph("graph.user.movies")
    List<User> findAll();
}
