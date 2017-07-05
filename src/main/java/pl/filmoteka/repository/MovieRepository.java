package pl.filmoteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.filmoteka.model.Movie;

import java.util.List;

/**
 * Repository for Movie entity.
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByName(String name);

    List<Movie> findByGenre(String genre);
}
