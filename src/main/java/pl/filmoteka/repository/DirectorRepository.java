package pl.filmoteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.filmoteka.model.Director;

import java.util.List;

/**
 * Created by Piotr on 29.03.2017.
 */
public interface DirectorRepository extends JpaRepository<Director, Long> {

    List<Director> findByName(String name);

    List<Director> findBySurname(String surname);

    List<Director> findByNameOrSurname(String name, String surname);
}
