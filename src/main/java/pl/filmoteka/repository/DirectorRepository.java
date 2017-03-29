package pl.filmoteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.filmoteka.model.Director;

/**
 * Created by Piotr on 29.03.2017.
 */
public interface DirectorRepository extends JpaRepository<Director, Long> {
}
